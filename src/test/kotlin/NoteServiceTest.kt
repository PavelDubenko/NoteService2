import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class NoteServiceTest {
    @Before
    fun clearBeforeTest(){
        NoteService.clear()
    }

    @Test
    fun addNoteTest() {
        val note = Note(title = "test", text = "test")
        NoteService.addNote(note)
        val result = NoteService.notes.size

        assertEquals(1, result)

    }

    @Test
    fun deleteNoteExistingNote() {
        val note = Note(title = "test", text = "test")
        NoteService.addNote(note)
        NoteService.deleteNote(1)
        val result = NoteService.notes.size

        assertEquals(0, result)
    }
    @Test(expected = NoteNotFoundException::class)
    fun deleteNoteNonExistingNote() {
        val note = Note(title = "test", text = "test")
        NoteService.addNote(note)
        NoteService.deleteNote(2)
    }

    @Test
    fun updateExistingNote() {
        NoteService.addNote(note = Note(title = "test", text = "test"))
        assertTrue(NoteService.updateNote(note = Note(id = 1, title = "test", text = "update test")))
    }
    @Test
    fun updateNonExistingNote() {
        NoteService.addNote(note = Note(title = "test", text = "test"))
        assertFalse(NoteService.updateNote(note = Note(id = 2, title = "test", text = "update test")))
    }

    @Test
    fun createCommentExistingNote() {
        val note = Note(title = "test", text = "test")
        NoteService.addNote(note)
        NoteService.createComment(1, comment = Comments(text = "test comment"))
        val result = note.comments.size

        assertEquals(1,result)
    }
    @Test(expected = NoteNotFoundException::class)
    fun createCommentNonExistingNote() {
        val note = Note(title = "test", text = "test")
        NoteService.addNote(note)
        NoteService.createComment(2, comment = Comments(text = "test comment"))
    }

    @Test
    fun deleteCommentExistingNote() {
        val note = Note(title = "test", text = "test")
        NoteService.addNote(note)
        NoteService.createComment(1, comment = Comments(text = "test comment"))
        NoteService.deleteComment(1,1)
        val result = note.comments.size

        assertEquals(0, result)
    }
    @Test(expected = NoteNotFoundException::class)
    fun deleteCommentNonExistingNote() {
        val note = Note(title = "test", text = "test")
        NoteService.addNote(note)
        NoteService.createComment(1, comment = Comments(text = "test comment"))
        NoteService.deleteComment(2,1)
    }

    @Test(expected = NoteNotFoundException::class)
    fun updateCommentNonExistingNote() {
        val note = Note(title = "test", text = "test")
        NoteService.addNote(note)
        NoteService.createComment(1, comment = Comments(text = "test comment"))
        NoteService.updateComment(2, comment = Comments(1, text = "updated text"))
    }
    @Test
    fun updateCommentExistingNote() {
        val note = Note(title = "test", text = "test")
        NoteService.addNote(note)
        NoteService.createComment(1, comment = Comments(text = "test comment"))
        assertTrue(NoteService.updateComment(1, comment = Comments(1, text = "updated text")))
    }

    @Test
    fun restoreDeletedComment() {
        val note = Note(title = "test", text = "test")
        NoteService.addNote(note)
        NoteService.createComment(1, comment = Comments(text = "test comment"))
        NoteService.deleteComment(1,1)
        assertTrue(NoteService.restoreComment(1,1))
    }
    @Test
    fun restoreNonDeletedComment() {
        val note = Note(title = "test", text = "test")
        NoteService.addNote(note)
        NoteService.createComment(1, comment = Comments(text = "test comment"))
        NoteService.deleteComment(1,1)
        assertFalse(NoteService.restoreComment(2,1))
    }
}