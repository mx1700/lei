//import java.io.File
//
//val idPos = "<input name=\"LastName\" type=\"hidden\" value=\""
//val dspPassPos = "<input name=\"\$dspPasswordDigest\" type=\"hidden\" value=\""
//val passPos = "<input name=\"PasswordDigest\" type=\"hidden\" value=\""
//val endPos = "\">";
//
//val httpPassPos = "<input name=\"HTTPPassword\" type=\"hidden\" value=\"("
//val dspHttpPassPos = "<input name=\"dspHTTPPassword\" type=\"hidden\" value=\"("
//val dspHttpPassPos2 = "<input name=\"\$dspHTTPPassword\" type=\"hidden\" value=\"(";
//val endPos2 = ")\">"
//
//fun main(args: Array<String>) {
//    val files = File("D:\\Users\\x1\\Desktop\\dbsource").listFiles()
//    val writer = File("d:\\r.csv").printWriter()
//    files.forEach { file ->
//        val c = file.readText()
//        writer.printf("%s,%s,%s,%s,%s,%s,%s\r\n",
//                file.name, getId(c),
//                getHttpPass(c), getDspHttpPass(c),
//                getPasswordDigest(c), getDspPasswordDigest(c),
//                getDspHttpPass2(c))
//    }
//}
//
//fun getId(content: String) = getSearchContent(content, idPos, endPos)
//fun getHttpPass(content: String) = getSearchContent(content, httpPassPos, endPos2)
//fun getDspHttpPass(c: String) = getSearchContent(c, dspHttpPassPos, endPos2)
//fun getDspHttpPass2(c: String) = getSearchContent(c, dspHttpPassPos2, endPos2)
//
//fun getPasswordDigest(c: String) = getSearchContent(c, passPos, endPos)
//fun getDspPasswordDigest(content: String) = getSearchContent(content, dspPassPos, endPos)
//
//fun getSearchContent(content: String,startPos: String, endPos: String): String? {
//    val start = content.indexOf(startPos);
//    if (start < 0) {
//        return ""
//    }
//    val end = content.indexOf(endPos, start)
//    return content.substring(start + startPos.length, end)
//}
//
