package io

import java.io.{BufferedInputStream, BufferedWriter, File, FileInputStream, FileOutputStream, FileWriter, IOException}
import java.nio.file.Paths
import java.sql.SQLException
import java.util.zip.{ZipEntry, ZipOutputStream}
import au.com.bytecode.opencsv.CSVWriter
import org.slf4j.{Logger, LoggerFactory}
import slick.jdbc.{GetResult, PositionedResult}

class CsvFileResult(fileName : String) extends GetResult[Unit] {

  val log: Logger = LoggerFactory.getLogger(this.getClass)

  /*
  * This is a bit of a hack, the apply function should be executed
  * for each row but because csvWriter.writeAll iterates through the
  * resultSet so it will be called once.
  *
  * in terms of performance:
  * if this service is expected to run very big queries I would
  * probably check how the writeAll function handles big datasets and will
  * change accordingly, maybe split the data to more then one file.
  *
  * you wrote in the docx doc about query limitation and I wasn't sure exactly
  * what you meant, if you wanted a max rows param in configuration, I would read the
  * config and append The LIMIT <num> to the query if it doesn't contain a valid limit already,
  * but now service runs all queries
  * so you can send what limit you want.
  *
  * */
  override def apply(positionedResult: PositionedResult): Unit = {
    val resultSet = positionedResult.rs
    val resultPath = new File(Paths.get(Paths.get("").toAbsolutePath.toString, "results/", this.fileName).toString)
    try {
      //pretty suitable scenario for try(resource) like in java, but doesn't seem to work here
      val outputFile = new BufferedWriter(new FileWriter(resultPath))
      val csvWriter = new CSVWriter(outputFile)
      csvWriter.writeAll(resultSet, true)
      csvWriter.flush()
      csvWriter.close()
      val fileName = resultPath.getName
      zip(resultPath, fileName.substring(0,fileName.lastIndexOf(".csv"))+ ".zip")
      log.info(s"The file $fileName has been created")
    } catch{
      case e: IOException => log.error(s"Error while trying to write to $fileName file \n${e.getMessage} " +
        s"\n${e.getStackTrace.mkString("(", ",\n ", ")")}}")
      case e : SQLException => log.error(s"Error while trying to read sql result set for writing to file" +
        s" \n${e.getMessage}\n${e.getStackTrace.mkString("(", ",\n ", ")")}")
      case e : Exception => log.error(s"Error while trying to convert result set to csv file" +
        s" \n${e.getMessage}\n${e.getStackTrace.mkString("(", ",\n", ")")}")
    }
  }

  private def zip(inFile : File, outFileName: String)  = {
    try{
    val zip = new ZipOutputStream(new FileOutputStream(Paths.get(inFile.getParent, outFileName).toString))
    zip.putNextEntry(new ZipEntry(inFile.getName))
    val in = new BufferedInputStream(new FileInputStream(inFile))
    var b = in.read()
    while (b > -1) {
      zip.write(b)
      b = in.read()
      }
      in.close()
      zip.closeEntry()
      zip.close()
      inFile.delete()
    } catch{
      case e: IOException => log.error(s"Error while trying to create zip file " +
        s"\n${e.getMessage}\n${e.getStackTrace.mkString("(", ",\n ", ")")}")
      case t: Throwable => throw t
    }
  }
}
