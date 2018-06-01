package com.github.leosilvadev.nonblockingjava.nonblocking.request

import com.github.leosilvadev.nonblockingjava.nonblocking.http.HTTPMethod
import spock.lang.Specification

/**
 * Created by leonardo on 5/31/18.
 */
class RequestDefinitionSpec extends Specification {

  def 'Should build a request definition with a valid definition'() {
    when:
    def maybe = RequestDefinition.from('GET /v1/users HTTP/1.1')

    then:
    maybe.isPresent()

    when:
    def definition = maybe.get()

    then:
    definition.version == 'HTTP/1.1'
    definition.path == '/v1/users'
    definition.method == HTTPMethod.GET
  }

  def 'Should NOT build a request definition on missing method'() {
    when:
    def maybe = RequestDefinition.from('/v1/users HTTP/1.1')

    then:
    !maybe.isPresent()
  }

  def 'Should NOT build a request definition on invalid method'() {
    when:
    def maybe = RequestDefinition.from('HUE /v1/users HTTP/1.1')

    then:
    !maybe.isPresent()
  }

  def 'Should NOT build a request definition on missing path'() {
    when:
    def maybe = RequestDefinition.from('GET HTTP/1.1')

    then:
    !maybe.isPresent()
  }

  def 'Should NOT build a request definition on invalid path'() {
    when:
    def maybe = RequestDefinition.from('GET mywrongpath/ HTTP/1.1')

    then:
    !maybe.isPresent()
  }

  def 'Should NOT build a request definition on missing http definition'() {
    when:
    def maybe = RequestDefinition.from('GET /v1/users')

    then:
    !maybe.isPresent()
  }

  def 'Should NOT build a request definition on invalid http definition'() {
    when:
    def maybe = RequestDefinition.from('GET /v1/users HTTP')

    then:
    !maybe.isPresent()
  }

  def 'Should NOT build a request definition on invalid http 1 definition'() {
    when:
    def maybe = RequestDefinition.from('GET /v1/users HTTP/2.0')

    then:
    !maybe.isPresent()
  }

}
