export const Question7 = () => {
  return (
    <section className="question">
      <h2>Question 7</h2>
      <div className="question-content">
        <strong>Instructions</strong>
        <div className="question-instructions">
          Explain to me in one paragraph in your own words what password hashing
          is, and why we do it? Give me an example of how you might implement it
          in TypeScript.
        </div>
        <hr />
        <div className="solution-section">
          <p>
            Hashing is a process in which we take some string of characters and
            transform it into another value. Hashing algorithms should be
            deterministic, so the same input should always produce the same
            output. Password hashing has an additional requirement on top of
            this: the information in the password is <strong>destroyed</strong>{" "}
            when put through the hashing algorithm. This means that you cannot
            take a password hash and work your way backwards to the original
            password. We hash passwords when storing them because it increases
            the password's security. If a malicious agent somehow got access to
            the DB, they wouldn't be able to see what the actual passwords for
            the users are, just their hashes.
          </p>

          <p>
            Note that the following example does not produce fixed-length
            output, nor does it actually do anything to prevent hash collisions
          </p>
          <pre>
            {`

const BIG_COMPOSITE_NUMBER = BIG_PRIME_NUMBER_1 * BIG_PRIME_NUMBER_2;

function hash(password: string): string {
  let hashed: number = 1;
  password.split("").forEach((c) => {
    const code = c.charCodeAt(0);
    const x = BIG_COMPOSITE_NUMBER ** code;
    hashed += hashed % x;
  })

  return hashed.toString();
}

`}
          </pre>
        </div>
      </div>
    </section>
  );
};
