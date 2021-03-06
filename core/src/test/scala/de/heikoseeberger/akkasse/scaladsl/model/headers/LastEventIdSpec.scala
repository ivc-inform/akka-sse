/*
 * Copyright 2015 Heiko Seeberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.heikoseeberger.akkasse
package scaladsl
package model
package headers

import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.testkit.RouteTest
import akka.http.scaladsl.testkit.TestFrameworkInterface.Scalatest
import org.scalatest.{ Matchers, WordSpec }

final class LastEventIdSpec extends WordSpec with Matchers with RouteTest with Scalatest {

  "Last-Event-ID" should {
    "match and extract the header value" in {
      Get().withHeaders(`Last-Event-ID`("123")) ~> route ~> check {
        status shouldBe OK
        responseAs[String] shouldBe "123"
      }
    }

    "reject the request if the header is not present" in {
      Get() ~> route ~> check {
        rejections should not be 'empty
      }
    }
  }

  private def route = {
    import Directives._
    headerValueByType[`Last-Event-ID`]() { case `Last-Event-ID`(id) => complete(id.toString) }
  }
}
