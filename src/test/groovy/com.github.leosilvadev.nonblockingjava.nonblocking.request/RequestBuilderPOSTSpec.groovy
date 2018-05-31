package com.github.leosilvadev.nonblockingjava.nonblocking.request

import com.github.leosilvadev.nonblockingjava.nonblocking.HttpMethod
import spock.lang.Specification

/**
 * Created by leonardo on 5/31/18.
 */
class RequestBuilderSpec extends Specification {

  def 'Should build a POST request with headers and body'() {
    given:
    def builder = new RequestBuilder()

    and:
    String[] lines = [
      'POST /v1/users HTTP/1.1',
      'Content-Type: application/json',
      '',
      '{"name":"Jao"}'
    ]

    when:
    def request = builder.build(lines)

    then:
    request.method == HttpMethod.POST
    request.path == '/v1/users'
    request.headers.size() == 1
    request.headers.get('Content-Type').get().value == 'application/json'
    request.body == '{"name":"Jao"}'
  }

  def 'Should build a POST request with headers but without a body'() {
    given:
    def builder = new RequestBuilder()

    and:
    String[] lines = [
      'POST /v1/users HTTP/1.1',
      'Content-Type: application/json'
    ]

    when:
    def request = builder.build(lines)

    then:
    request.method == HttpMethod.POST
    request.path == '/v1/users'
    request.headers.size() == 1
    request.headers.get('Content-Type').get().value == 'application/json'
    request.body == ''
  }

  def 'Should build a POST request without headers and body'() {
    given:
    def builder = new RequestBuilder()

    and:
    String[] lines = [
      'POST /v1/users HTTP/1.1'
    ]

    when:
    def request = builder.build(lines)

    then:
    request.method == HttpMethod.POST
    request.path == '/v1/users'
    request.headers.size() == 0
    request.body == ''
  }

  def 'Should build a POST request without headers but with body'() {
    given:
    def builder = new RequestBuilder()

    and:
    String[] lines = [
      'POST /v1/users HTTP/1.1',
      '',
      '{"name":"Jao"}'
    ]

    when:
    def request = builder.build(lines)

    then:
    request.method == HttpMethod.POST
    request.path == '/v1/users'
    request.headers.size() == 0
    request.body == '{"name":"Jao"}'
  }

}
