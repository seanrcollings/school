import { useState } from "react";

export const Question1 = () => {
  const [counter, setCounter] = useState(0);

  return (
    <section className="question">
      <h2>Question 1</h2>
      <div className="question-content">
        <strong>Instructions</strong>
        <div className="question-instructions">
          In space below, build a basic counter application that does the
          following:
          <ol>
            <li>Allows me to increment the count by 1</li>
            <li>Allows me to decrement the count by 1</li>
            <li>Allows me to reset the count to 0</li>
            <li>
              The count should be displayed and updated as I press the buttons
            </li>
          </ol>
          Ensure that you are correctly following unidirectional dataflow.
        </div>
        <hr />
        <div className="solution-section">
          <p>{counter}</p>
          <button onClick={() => setCounter(counter - 1)}>-</button>
          <button onClick={() => setCounter(counter + 1)}>+</button>
          <button onClick={() => setCounter(0)}>reset</button>
        </div>
      </div>
    </section>
  );
};
