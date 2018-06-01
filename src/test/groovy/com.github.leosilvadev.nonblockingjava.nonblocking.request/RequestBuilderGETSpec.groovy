package com.github.leosilvadev.nonblockingjava.nonblocking.request

import com.github.leosilvadev.nonblockingjava.nonblocking.http.HTTPMethod
import spock.lang.Specification

/**
 * Created by leonardo on 5/31/18.
 */
class RequestBuilderGETSpec extends Specification {

  def 'Should build a GET request with headers and body, but ignore the body'() {
    given:
    def builder = new RequestBuilder()

    and:
    def lines = [
      'GET /v1/users HTTP/1.1',
      'Content-Type: application/json',
      'Accept: application/json',
      '',
      '{"name":"Jao"}'
    ]

    when:
    def request = builder.build(lines)

    then:
    request.method == HTTPMethod.GET
    request.path == '/v1/users'
    request.headers.size() == 2
    request.headers.get('content-type').get().value == 'application/json'
    request.headers.get('accept').get().value == 'application/json'
    request.body == ''
  }

  def 'Should build a GET request with headers but without a body'() {
    given:
    def builder = new RequestBuilder()

    and:
    def lines = [
      'GET /v1/users HTTP/1.1',
      'Content-Type: application/json'
    ]

    when:
    def request = builder.build(lines)

    then:
    request.method == HTTPMethod.GET
    request.path == '/v1/users'
    request.headers.size() == 1
    request.headers.get('content-type').get().value == 'application/json'
    request.body == ''
  }

  def 'Should build a GET request without headers and body'() {
    given:
    def builder = new RequestBuilder()

    and:
    def lines = [
      'GET /v1/users HTTP/1.1'
    ]

    when:
    def request = builder.build(lines)

    then:
    request.method == HTTPMethod.GET
    request.path == '/v1/users'
    request.headers.size() == 0
    request.body == ''
  }

}
