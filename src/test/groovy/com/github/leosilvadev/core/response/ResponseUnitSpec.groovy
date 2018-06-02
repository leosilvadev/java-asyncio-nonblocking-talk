package com.github.leosilvadev.core.response

import com.github.leosilvadev.core.http.HTTPStatus
import com.github.leosilvadev.core.http.Header
import com.github.leosilvadev.core.http.Headers
import spock.lang.Specification

/**
 * Created by leonardo on 6/1/18.
 */
class ResponseUnitSpec extends Specification {

  def 'Should build output when status is ok, has no header and no body'() {
    given:
    def response = new Response(HTTPStatus.OK, new Headers(), "")

    when:
    def output = response.toString()

    then:
    output == "HTTP/1.1 200 OK\n\n"
  }

  def 'Should build output when status is ok, has headers but no body'() {
    given:
    def headers = new Headers()
      .add(new Header("Content-Type", "application/json"))
      .add(new Header("Server", "NIO/Server"))

    and:
    def response = new Response(HTTPStatus.OK, headers, null)

    when:
    def output = response.toString()

    then:
    output == "HTTP/1.1 200 OK\nContent-Type: application/json\nServer: NIO/Server\n\n"
  }

  def 'Should build output when status is ok, has headers and has body'() {
    given:
    def headers = new Headers()
      .add(new Header("Content-Type", "application/json"))
      .add(new Header("Server", "NIO/Server"))

    and:
    def response = new Response(HTTPStatus.OK, headers, "{\"name\":\"JAO\"}")

    when:
    def output = response.toString()

    then:
    output == "HTTP/1.1 200 OK\nContent-Type: application/json\nServer: NIO/Server\n\n{\"name\":\"JAO\"}"
  }

}
