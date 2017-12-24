//
// Scaled TODO Mode - a Scaled major mode for editing TODO files
// http://github.com/scaled/todo-mode/blob/master/LICENSE

package scaled.todo

import scaled._
import scaled.grammar._
import scaled.major.TextConfig

object TodoConfig extends Config.Defs {

  /** The CSS style applied to `done` list entries. */
  val doneStyle = "textDoneFace"
}

@Plugin(tag="textmate-grammar")
class TodoGrammarPlugin extends GrammarPlugin {
  import TextConfig._
  import TodoConfig._

  def grammars = Map("text.todo" -> "unused")

  override def grammar (scopeName :String) = scopeName match {
    case "text.todo" => new Grammar(
      name      = "TODO",
      scopeName = "text.todo",
      foldingStartMarker = None,
      foldingStopMarker  = None) {

      val repository = Map[String,Rule]()

      // we have to specify a return type here to work around scalac bug; meh
      val patterns :List[Rule] = List(
        single("""^\* .*$""", name=Some("markup.heading")),
        single("""^\*\* .*$""", name=Some("markup.subheading")),
        single("""^\s*- .*$""", name=Some("markup.list")),
        single("""^\s*x .*$""", name=Some("markup.list.complete"))
      )
    }
    case _ => super.grammar(scopeName)
  }

  override def effacers = List(
    effacer("markup.heading", headerStyle),
    effacer("markup.subheading", subHeaderStyle),
    // effacer("markup.list", listStyle),
    effacer("markup.list.complete", doneStyle)
  )
}

@Major(name="todo",
       tags=Array("text", "project", "todo"),
       pats=Array("TODO.*"),
       desc="A major mode for editing TODO files.")
class TodoMode (env :Env) extends GrammarTextMode(env) {

  override def dispose () {} // nada for now

  override def configDefs = TodoConfig :: super.configDefs
  override def stylesheets = stylesheetURL("/todo.css") :: super.stylesheets
  override def langScope = "text.todo"
}
