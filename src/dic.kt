//import java.io.File
//import kotlin.coroutines.experimental.buildSequence
//import org.h2.jdbcx.JdbcConnectionPool
//import java.sql.Connection
//import java.sql.ResultSet
//import java.sql.DatabaseMetaData
//
//
//
//fun main(args: Array<String>) {
////    val lines = File("C:\\Users\\x1\\pwd_dic.txt").readLines()
////    println("read OK")
////    readLine()
////    db()
//    aaa()
//}
//
//fun db() {
//
//
////    val stmt = conn.prepareStatement("INSERT INTO DIC VALUES(?)")
////    stmt.setString(1, "abc")
////    stmt.execute()
////
////    val q = conn.prepareStatement("SELECT pwd FROM DIC");
////    val rs = q.executeQuery()
////    while (rs.next()) {
////        val pwd = rs.getString(1)
////        println(pwd)
////    }
//}
//
//fun getConn(): Connection {
//    val dbPath = "./pwd"
//    val pool = JdbcConnectionPool.create("jdbc:h2:" + dbPath, "sa", "")
//    val conn = pool.getConnection()
//    val meta = conn.getMetaData()
//    val rsTables = meta.getTables(null, null, "DIC",
//            arrayOf("TABLE"))
//    if (!rsTables.next()) {
//        println("create table DIC")
//        val stmt = conn.createStatement();
//        stmt.execute("CREATE TABLE DIC(pwd VARCHAR(50))");
//    }
//    return conn;
//}
//
//fun insert(pwd: String, conn: Connection) {
//    val stmt = conn.prepareStatement("INSERT INTO DIC VALUES(?)")
//    stmt.setString(1, pwd)
//    stmt.execute()
//}
//
//fun aaa() {
//    val lines = buildSequence {
//        File("D:\\pwd").listFiles().forEach {
//            it.useLines {
//                for(line in it) {
//                    yield(line)
//                }
//            }
//            println("${it.name} OK!")
//        }
//    }
//
////    val writer = File("C:\\Users\\x1\\pwd_dic.txt").printWriter()
//    val conn = getConn()
//    for (line: String in lines) {
//        val l = line.trim()
//        if (l.length > 22) {
//            continue
//        }
////        writer.println(l)
//        insert(l, conn)
//    }
//}
//
