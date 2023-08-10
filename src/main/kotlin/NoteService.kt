data class Note(
    val id: Int = 0,
    val title: String,
    val text:String,
    var comments: MutableList<Comments> = mutableListOf()
)
data class Comments(
    val id: Int = 0,
    val canPost: Boolean = true,
    val groupsCanPost:Boolean = true,
    val canClose:Boolean = true,
    val canOpen:Boolean = true,
    val text:String,
    var deleted:Boolean = false
)
class NoteNotFoundException(s: String) : Throwable()

object NoteService {

    var notes = mutableListOf<Note>()
    var lastNoteId = 0
    var deletedComments = mutableListOf<Comments>()
    var lastCommentId = 0


    fun addNote(note: Note):Note {                     //добавляет заметку
        notes += note.copy(id = ++lastNoteId)
        return notes.last()
    }

    fun deleteNote(id: Int) {                          //удаляет заметку по ID
        val deletedNote = notes.find { it.id == id }
        if (deletedNote == null) {
            throw NoteNotFoundException("Note with id $id not found")
        } else {
            notes.remove(deletedNote)
        }
    }
    fun updateNote(note: Note): Boolean {             //обновляет заметку
        for ((index, oldNote) in notes.withIndex()){
            if (oldNote.id == note.id) {
                notes[index] = note.copy()
                return true
            }
        }
        return false
    }
    fun printAllNotes(){                              //выводит все заметки
        for (note in notes)
            println(note)
    }

    fun createComment(id: Int, comment: Comments):Comments { //добавляет коммент по ID заметки
        val commentedNote = notes.find { it.id == id }
        if(commentedNote == null){
            throw NoteNotFoundException("Заметка с ID$id не найдена")
        } else {
            commentedNote.comments += comment.copy(id = ++lastCommentId)
        }
        return comment
    }
    fun deleteComment(noteId:Int, commentId:Int) { //удаляет коммент по ID заметки и ID коммента
        val commentedNote = notes.find { it.id == noteId }
        if (commentedNote == null){
            throw NoteNotFoundException("Заметка с ID$noteId не найдена")
        } else {
            val deletedComment = commentedNote.comments.find { it.id == commentId }
            if (deletedComment != null) {
                deletedComments.add(deletedComment.copy(deleted = true))
            }
            commentedNote.comments.remove(deletedComment)
        }
    }
    fun updateComment(noteId: Int, comment: Comments):Boolean { //обновляет комментарий по ID заметки
        val commentedNote = notes.find { it.id == noteId }
        if (commentedNote == null) {
            throw NoteNotFoundException("Заметка с ID$noteId не найдена")
        } else {
            var updatedComment = commentedNote.comments.find { it.id == comment.id }
            commentedNote.comments.remove(updatedComment)
            updatedComment = comment.copy()
            commentedNote.comments.add(updatedComment)
            return true
        }
    }
    fun restoreComment(commentId: Int, noteId: Int):Boolean { //восстанавливает удаленный комментарий
        val restoredComment = deletedComments.find {it.id == commentId}
        val updatedNote = notes.find { it.id == noteId }
        if (restoredComment != null) {
            if (updatedNote != null) {
                updatedNote.comments.add(restoredComment)
                return true
            }
        }
        return false
    }
    fun clear() {
        notes = mutableListOf()
        deletedComments = mutableListOf()
        lastNoteId = 0
        lastCommentId = 0
    }


}

fun main(){
    NoteService.addNote(note = Note(title = "Заметка", text = "Купить продукты"))
    NoteService.createComment(1, comment = Comments(text = "готово"))
    NoteService.createComment(1, comment = Comments(text = "готово"))
    NoteService.printAllNotes()
    NoteService.deleteComment(1,1)
    NoteService.printAllNotes()
    NoteService.restoreComment(1,1)
    NoteService.printAllNotes()

}