/*
 * Copyright 2011-2017 Chris de Vreeze
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.cdevreeze.xpathparser.parse

import eu.cdevreeze.xpathparser.ast.EQName
import eu.cdevreeze.xpathparser.common.EName
import fastparse.NoWhitespace._

/**
 * EQName parsing support. Note that EQNames are non-delimiting terminal symbols.
 * No whitespace is skipped during parsing of an EQName.
 *
 * For re-usability without imposing any NoCut calls on using parsers, no cuts have been used.
 *
 * @author Chris de Vreeze
 */
object EQNames {
  import fastparse._

  private val DT = DelimitingTerminals

  // We could change the order of the 2 branches below, but I'd rather explicitly use a small lookahead.

  def eqName[_: P]: P[EQName] =
    P(!"Q{" ~ qName | uriQualifiedName)

  def qName[_: P]: P[EQName.QName] =
    P(NCNames.ncName ~ (":" ~ NCNames.ncName).?).map {
      case (s1, s2Opt) =>
        if (s2Opt.isEmpty) {
          EQName.QName.parse(s1.name)
        } else {
          EQName.QName.parse(s1.name + ":" + s2Opt.get.name)
        }
    }

  def uriQualifiedName[_: P]: P[EQName.URIQualifiedName] =
    P(DT.bracedUriLiteral ~ NCNames.ncName).map {
      case (uriLit, localPart) =>
        EQName.URIQualifiedName(EName(uriLit.namespaceOption, localPart.name))
    }
}
