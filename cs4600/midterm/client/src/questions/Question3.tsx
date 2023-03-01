import { useState } from "react";

export const Question3 = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  return (
    <section className="question">
      <h2>Question 3</h2>
      <div className="question-content">
        <strong>Instructions</strong>
        <div className="question-instructions">
          <div>Identify and fix the bug in this component.</div>
          <div>
            Start by interacting with the text inputs and it will be obvious
            that something is wrong.
          </div>
          <div>
            Ensure that you are correctly following unidirectional dataflow.
          </div>
        </div>
        <hr />
        <div className="solution-section">
          <label>
            Email
            <input
              type="text"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </label>
          <label>
            Password
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </label>
        </div>
      </div>
    </section>
  );
};
