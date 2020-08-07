package service

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait QueryRequestJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val queryRequestFormat: RootJsonFormat[QueryToFileRequest] = jsonFormat2(QueryToFileRequest)
}

final case class QueryToFileRequest(query : String, fileName : String) {
  def getQuery: String = this.query
  def getFileName : String = this.fileName
}
