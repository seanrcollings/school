export const Question8 = () => {
  return (
    <section className="question">
      <h2>Question 8</h2>
      <div className="question-content">
        <strong>Instructions</strong>
        <div className="question-instructions">
          My colleague has told me that their software is perfect and has no
          bugs, and therefore it is unessasary for them to hash passwords
          because their database will never get leaked.
          <div>
            Assume, for the sake of argument, that the software is, in fact,
            perfect with no issues. Do you agree with my colleages assessment?
            Why or why not? Be sure to provide specific examples.
          </div>
        </div>
        <hr />
        <div className="solution-section">
          Regardless of how "perfect" their code is, not hashing the passwords
          is still a bad practice, what follows are some reasons why:
          <ol>
            <li>
              The code base currently having no problems does not guarantee that
              it will always remain that way
            </li>
            <li>
              Other components in the system that the software resides may have
              exploits, allowing someone to gain access
            </li>
            <li>
              It makes the job of a malicious agent easier, as they could
              theoretically social engineer DB credentials out of someone with
              them and use them to find ever user's passwords
            </li>
          </ol>
        </div>
      </div>
    </section>
  );
};
