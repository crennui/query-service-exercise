package io

trait RequestHandler[T, U] extends Any {
  def executeRequest(request: T): U
}
