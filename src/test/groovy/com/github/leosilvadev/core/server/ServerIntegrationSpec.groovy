package com.github.leosilvadev.core.server

import com.github.leosilvadev.core.Core
import com.github.leosilvadev.core.config.BlockingConfig
import com.github.leosilvadev.core.config.Configuration
import com.github.leosilvadev.core.config.ServerConfig
import com.github.leosilvadev.core.json.Json
import com.github.leosilvadev.core.response.Response
import io.reactivex.Single
import io.reactivex.functions.Consumer
import spock.lang.Specification

import static io.restassured.RestAssured.given
import static io.restassured.RestAssured.when
import static org.hamcrest.Matchers.equalTo

/**
 * Created by leonardo on 6/2/18.
 */
class ServerIntegrationSpec extends Specification {

  Core core
  ServerConfigurer serverConfigurer
  Server runningServer

  def setup() {
    def configuration = new Configuration().with {
      server = new ServerConfig("localhost", 9000)
      blocking = new BlockingConfig(1)
      it
    }
    core = Core.config(configuration)
    serverConfigurer = Server.config(core)
  }

  def 'Should return a list of users as json'() {
    setup:
    serverConfigurer.handleGet('/v1/users') { request ->
      Single.just([[name: 'JAO', age: 30], [name: 'MARIA', age: 31]]).map { Response.ok().json(it).build() }
    }

    when:
    serverConfigurer.start().subscribe({ runningServer = it } as Consumer)

    and:
    def response = when().get('http://localhost:9000/v1/users')

    then:
    response.then()
      .statusCode(200)
      .contentType('application/json')
      .body(equalTo('[{"name":"JAO","age":30},{"name":"MARIA","age":31}]'))
  }

  def 'Should send and receive a list of users as json'() {
    setup:
    serverConfigurer.handlePost('/v1/users') { request ->
      def users = Json.parse(request.body, List).orElse([])
      Single.just(users).map { Response.ok().json(it).build() }
    }

    when:
    serverConfigurer.start().subscribe({ runningServer = it } as Consumer)

    and:
    def response = given()
      .contentType('application/json')
      .body(Json.toJson([[name: 'JAO', age: 30], [name: 'MARIA', age: 31]]))
      .post('http://localhost:9000/v1/users')

    then:
    response.then()
      .statusCode(200)
      .body(equalTo('[{"name":"JAO","age":30},{"name":"MARIA","age":31}]'))
  }

  def cleanup() {
    runningServer.shutdown()
    core.shutdown()
  }

}
