import org.apache.commons.lang3.StringEscapeUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil.close
import com.opencsv.CSVWriter
import java.io.FileWriter
import java.io.Writer
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.io.BufferedWriter





fun main(args: Array<String>) {
    //val files = args.map { File(it) };
    val files = File("D:\\lei2\\OA接入网扫描168.52.6网段-指定IP\\142_兴业银行6段ip_2018_05_03_html\\host").listFiles()
//    val args = arrayOf("D:\\Users\\x1\\Desktop\\lei\\接入网\\453_辽宁-联通-复测-接入网网管-全端口_2017_11_06_html\\host\\132.193.40.52.html", "D:/Users/x1/Desktop/123/host/192.168.10.43.html");
    val htmlFiles = files.filter { it.isFile && it.extension == "html" }
    error("Files: " + htmlFiles.map { it.name }.joinToString(", "))
    error()
    val hosts = htmlFiles.map { Host(it.readText()) }
    output(getLines(hosts))
    error("结束")
//    htmlFiles.forEach(::getFileResult)
}

fun output(lines: List<List<String?>>) {
    val file = File("d:\\write.csv")
    val writer = BufferedWriter(
            OutputStreamWriter(
                    FileOutputStream(file), "GBK"))
    val csvWriter = CSVWriter(writer, ',')
    for (line in lines) {
        csvWriter.writeNext(line.toTypedArray())
    }
    csvWriter.close()
}

fun getLines(hosts: List<Host>): List<List<String?>> {
    val header = listOf(
            "IP", "操作系统", "漏洞评分", "主机评分",
            "端口", "协议" ,"服务",
            "漏洞等级", "漏洞名称",
            "详细描述", "解决办法", "威胁分值",
            "CNVD", "CVE", "CVSS"
    )
    val result = mutableListOf<List<String?>>(header)
    for (host in hosts) {
        for (row in host.rows) {
            for (vul in row.vulnerabilityItems) {
                result.add(listOf(
                        host.ip, host.os, host.louDongScore, host.zhuJiScore,
                        row.port, row.protocol, row.service,
                        vul.level, vul.title,
                        vul.desc, vul.answer, vul.score,
                        vul.CNVD, vul.CVE, vul.CVSS
                ))
            }
        }
    }
    return result
}

class Host(private val html: String) {
    val ip: String? by lazy {
        getMatch(html, "IP地址</th>\\s+<td>(\\S+)</td>".toRegex())
    }

    val os: String? by lazy {
        getMatch(html, "操作系统</th>\\s+<td>([\\S\\s]+?)</td>".toRegex())
    }

    val louDongScore: String? by lazy {
        getMatch(html, "漏洞风险评估分</th>\\s+<td>([\\S\\s]+?)</td>".toRegex())
    }

    val zhuJiScore: String? by lazy {
        getMatch(html, "主机风险评估分</th>\\s+<td>(\\S+)</td>".toRegex())
    }

    val rows: List<Row> by lazy {
        val reg = """
        <td class="vul_port">(\S*)</td>\s+<td>(\S*)</td>\s+<td>(\S*)</td>\s+<td>\s+?(<ul>[\S\s]+?</ul>)
        """.trim().toRegex()
        reg.findAll(html).map {
            Row(it.groups[1]!!.value, it.groups[2]!!.value, it.groups[3]!!.value, it.groups[4]!!.value, html)
        }.toList()
    }
}

class Row(val port: String, val protocol: String, val service: String, vulnerability: String, private val html: String) {
    val vulnerabilityItems: List<Vulnerability> by lazy {
        val reg = """
        <span class="level_danger_(\w+)" onclick="show_vul\('\w+','(\w+)'\);"[\S\s]+?>([\S\s]+?)</span>
        """.trim().toRegex()
        reg.findAll(vulnerability).map { Vulnerability(it.groups[1]!!.value, it.groups[3]!!.value, getTableContent(it.groups[2]!!.value)) }.toList()
    }

    private fun getTableContent(name: String): String {
        return getMatch(html, "id=\"$name\"[\\S\\s]+?(<table[\\S\\s]+?</table>)".toRegex())!!;
    }
}

class Vulnerability(val level: String, val title: String, private val extTableName: String) {
    /**
     * a)     详细描述
    b)     解决办法
    c)      威胁分值
    d)     CVE编号
    e)     CVSS评分
    f)      CNVD编号
     */
    val desc: String? by lazy {
        getMatch(extTableName, "详细描述</th>\\s+<td>([\\S\\s]+?)</td>".toRegex())
    }

    val answer: String? by lazy {
        getMatch(extTableName, "解决办法</th>\\s+<td>([\\S\\s]+?)</td>".toRegex())
    }

    val score: String? by lazy {
        getMatch(extTableName, "威胁分值</th>\\s+<td>([\\S\\s]+?)</td>".toRegex())
    }

    val CVE: String? by lazy {
        getMatch(extTableName, "CVE编号</th>\\s+<td><a[\\S\\s]+?>([\\S\\s]+?)</a></td>".toRegex())
    }

    val CVSS: String? by lazy {
        getMatch(extTableName, "CVSS评分</th>\\s+<td>([\\S\\s]+?)</td>".toRegex())
    }

    val CNVD: String? by lazy {
        getMatch(extTableName, "CNVD编号</th>\\s+<td><a[\\S\\s]+?>([\\S\\s]+?)</a></td>".toRegex())
    }
}

fun getMatch(content: String, reg: Regex): String? {
    val matches = reg.find(content)
    return if (matches == null || matches.groups[1] == null) {
        null
    } else {
        matches.groups[1]!!.value.trim()
    }
}




//---------------------------------------

//fun getFileResult(file: File) {
//    val content = file.readText();
//    val ip = getIp(content)
//    if (ip == null) {
//        error("File ${file.name} not found ip!")
//        error()
//        return
//    }
//    //var rows = getRows(content)
//    //if (rows.count() == 0) {
//    val rows = getRows2(content)
//    //}
//    debug("rows:" + rows.count())
//    error("$ip\t${rows.sumBy { it.vulnerabilityItems.count() }}")
//    printResult(ip, rows)
//}

//fun printResult(ip: String, titles: List<Row>) {
//    titles.forEach {
//        val row = it
//        row.vulnerabilityItems.forEach {
//            result("$ip\t \t${row.port}\t${row.protocol}\t${row.service}\t${StringEscapeUtils.unescapeHtml4(it.title)}\t${getLevelName(it.level)}")
//        }
//    }
//}

fun getLevelName(t: String) = when(t) {
    "high" -> "高"
    "middle" -> "中"
    "low" -> "低"
    else -> ""
}


//fun getRows(content: String): List<Row> {
//    val reg = """
//        <td class="vul_port">(\S*)</td>\s+<td>(\S*)</td>\s+<td>(\S*)</td>\s+<td colspan="2">\s+(<table [\S\s]+?</table>)
//        """.trim().toRegex()
//    return reg.findAll(content).map {
//        Row(it.groups[1]!!.value, it.groups[2]!!.value, it.groups[3]!!.value, it.groups[4]!!.value, content)
//    }.toList()
//}


fun result(msg: String = "") {
    println(msg)
}

fun error(msg: String = "") {
    System.err.println(msg)
    System.err.flush()
}

fun debug(msg: String = "") {
    println("DEBUG\t" + msg);
}