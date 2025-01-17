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

import eu.cdevreeze.xpathparser.ast.AnyWildcard
import eu.cdevreeze.xpathparser.ast.LocalNameWildcard
import eu.cdevreeze.xpathparser.ast.NamespaceWildcard
import eu.cdevreeze.xpathparser.ast.PrefixWildcard
import eu.cdevreeze.xpathparser.ast.Wildcard
import fastparse.NoWhitespace._

/**
 * Wildcard parsing support. No whitespace skipping is performed. See ws:explicit constraint.
 *
 * For re-usability without imposing any NoCut calls on using parsers, no cuts have been used.
 *
 * @author Chris de Vreeze
 */
object Wildcards {
  import fastparse._

  private val DT = DelimitingTerminals

  // Mind the order of parsing, trying to match AnyWildcard only at the end

  def wildcard[_: P]: P[Wildcard] =
    P(prefixWildcard | localNameWildcard | namespaceWildcard | anyWildcard)

  def prefixWildcard[_: P]: P[PrefixWildcard] =
    P(NCNames.ncName ~ DT.colonAsterisk) map {
      nm => PrefixWildcard(nm)
    }

  def localNameWildcard[_: P]: P[LocalNameWildcard] =
    P(DT.asteriskColon ~ NCNames.ncName) map {
      nm => LocalNameWildcard(nm)
    }

  def namespaceWildcard[_: P]: P[NamespaceWildcard] =
    P(DT.bracedUriLiteral ~ DT.asterisk) map {
      uriLit => NamespaceWildcard(uriLit)
    }

  def anyWildcard[_: P]: P[AnyWildcard.type] =
    P(DT.asterisk) map (_ => AnyWildcard)
}
