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

import eu.cdevreeze.xpathparser.ast.NCName
import fastparse.NoWhitespace._

/**
 * NCName parsing support. Note that NCNames are non-delimiting terminal symbols.
 * No whitespace is skipped during parsing of an NCName.
 *
 * @author Chris de Vreeze
 */
object NCNames {
  import fastparse._

  def ncName[_: P]: P[NCName] =
    P(CharPred(c => NCName.canBeStartOfNCName(c)).! ~ CharPred(c => NCName.canBePartOfNCName(c)).rep.!) map {
      case (s1, s2) => NCName(s1 + s2)
    }
}
