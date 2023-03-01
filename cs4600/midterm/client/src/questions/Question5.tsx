import { useEffect, useState } from "react";

const random = () => Math.round(Math.random() * 1000);

export const Question5 = () => {
  const [number, setNumber] = useState<number | null>(null);
  const [expression, setExpression] = useState("");

  // The question mentions needing a useEffect hook
  // In this situation, wouldn't actually need one because
  // you could set the default value in the useState call,
  // But this way you could swap it setNumber below with
  // a network call and the component would behave the same
  useEffect(() => setNumber(random()), []);

  const applyExpression = () => {
    if (!number) return;
    const opt = expression[0];
    const operand = parseInt(expression.slice(1), 10);
    switch (opt) {
      case "+":
        setNumber(number + operand);
        break;
      case "-":
        setNumber(number - operand);
        break;
    }
  };

  return (
    <section className="question">
      <h2>Question 5</h2>
      <div className="question-content">
        <strong>Instructions</strong>
        <div className="question-instructions">
          It is common to do something when the page first loads - build me some
          UI that does the following
          <ol>
            <li>
              When the page first loads generate a random number and display it
              on the screen
            </li>
            <li>
              Give me a text input that allows me to type ether a + or -
              followed by a number
              <ol type="a">
                <li>For example: "+1", "-2", "+10", "-19.2"</li>
                <li>
                  Don't worry about handling bad inputs for this questions
                </li>
              </ol>
            </li>
            <li>Give me a button I can push that says "Evaluate"</li>
            <li>
              When I push the button parse the expression I typed in and either
              add or subtract the value from the random number that was
              generated and display replace the number on the screen with the
              result.
            </li>
            <li>Give me a button to generate a new random number.</li>
          </ol>
          <div>Be sure to follow unidirectional dataflow!</div>
          Hint: you will need a useEffect hook.
        </div>
        <hr />
        <div className="solution-section">
          <div>Number: {number}</div>
          <input
            type="text"
            value={expression}
            onChange={(e) => setExpression(e.target.value)}
          />
          <button onClick={applyExpression} disabled={!number}>
            Evaluate
          </button>
          <button onClick={() => setNumber(random())}>Randomize</button>
        </div>
      </div>
    </section>
  );
};
