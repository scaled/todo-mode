//
// Scaled TODO Mode - a Scaled major mode for editing TODO files
// http://github.com/scaled/todo-mode/blob/master/LICENSE

package scaled.todo

import scaled._
import scaled.grammar._
import scaled.major.TextConfig

object TodoConfig extends Config.Defs {
  import TextConfig._
  import GrammarConfig._

  /** The CSS style applied to `done` list entries. */
  val doneStyle = "textDoneFace"

  // map TextMate grammar scopes to Scaled style definitions
  val effacers = List(
    effacer("markup.heading", sectionStyle),
    // effacer("markup.list", listStyle),
    effacer("markup.list.complete", doneStyle)
  )

  val todoGrammar = new Grammar(
    name      = "TODO",
    scopeName = "text.todo",
    foldingStartMarker = None,
    foldingStopMarker  = None) {

    val repository = Map[String,Rule]()

    // we have to specify a return type here to work around scalac bug; meh
    val patterns :List[Rule] = List(
      single("""^\* .*$""", name=Some("markup.heading")),
      single("""^\s*- .*$""", name=Some("markup.list")),
      single("""^\s*x .*$""", name=Some("markup.list.complete"))
    )
  }

  lazy val grammars = Seq(todoGrammar)
}

@Major(name="todo",
       tags=Array("project", "todo"),
       pats=Array("TODO.*"),
       desc="A major mode for editing TODO files.")
class TodoMode (env :Env) extends GrammarTextMode(env) {

  override def dispose () {} // nada for now

  override def configDefs = TodoConfig :: super.configDefs
  override def stylesheets = stylesheetURL("/todo.css") :: super.stylesheets
  override protected def grammars = TodoConfig.grammars
  override protected def effacers = TodoConfig.effacers
}
