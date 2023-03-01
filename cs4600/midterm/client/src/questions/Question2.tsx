import { useRef, useState } from "react";

interface Note {
  id: number;
  description: string;
}

export const Question2 = () => {
  const [text, setText] = useState("");
  const [notes, setNotes] = useState<Note[]>([]);
  // Maintains our fake id counter as a ref
  // so it doesn't reset on a hot reload
  const id = useRef(0);

  const addNote = (description: string) => {
    const note: Note = {
      id: id.current++,
      description,
    };
    setNotes([...notes, note]);
  };

  const deleteNote = (id: number) => {
    console.log(id);
    setNotes(notes.filter((n) => n.id !== id));
  };

  return (
    <section className="question">
      <h2>Question 2</h2>
      <div className="question-content">
        <strong>Instructions</strong>
        <div className="question-instructions">
          You are going to build me a simple note taking application that does
          the following
          <ol>
            <li>Gives me an input where I can type my note</li>
            <li>Gives me a save button</li>
            <li>
              When the save button is pressed the following should happen:
              <ol type="a">
                <li>The text I typed into the input field gets cleared out.</li>
                <li>
                  The note I typed gets displayed in a list of notes on the
                  screen
                </li>
              </ol>
            </li>
            <li>
              On each note, there should be a button that allows me to delete
              the note from the list.
            </li>
          </ol>
          Ensure that you are correctly following unidirectional dataflow.
        </div>
        <hr />
        <div className="solution-section">
          <div>
            <input
              type="text"
              value={text}
              onChange={(e) => setText(e.target.value)}
            />
            <button
              onClick={() => {
                if (text === "") return;
                setText("");
                addNote(text);
              }}
            >
              Submit
            </button>
          </div>
          <div>
            {notes.map((note) => (
              <div key={note.id}>
                <span style={{ paddingRight: "5px" }}>
                  {note.id}. {note.description}
                </span>
                <button onClick={() => deleteNote(note.id)}>Delete</button>
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  );
};
