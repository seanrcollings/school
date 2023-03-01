import {
  ChangeEventHandler,
  FormEventHandler,
  useEffect,
  useState,
} from "react";

interface Todo {
  id: number;
  content: string;
  isCompleted: boolean;
}

interface CreateTodo {
  content: string;
  isCompleted?: boolean;
}

interface UpdateTodo {
  isCompleted: boolean;
}

class TodosApi {
  static async list(): Promise<Todo[]> {
    const res = await fetch("http://localhost:8000/todos", {
      headers: { "Content-Type": "application/json" },
    });
    const data = await res.json();
    return data.todos;
  }

  static async create(data: CreateTodo): Promise<Todo> {
    const res = await fetch("http://localhost:8000/todos", {
      method: "post",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });
    const json = await res.json();
    return json.todo;
  }

  static async update(id: number, data: UpdateTodo): Promise<Todo> {
    const res = await fetch(`http://localhost:8000/todos/${id}`, {
      method: "put",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });
    const json = await res.json();
    return json.todo;
  }
}

export const Question9 = () => {
  const [todos, setTodos] = useState<Todo[]>([]);
  const [inputText, setInputText] = useState("");

  const getTodos = async () => {
    const todos = await TodosApi.list();
    setTodos(todos);
  };

  useEffect(() => {
    getTodos();
  }, []);

  const submitTodo: FormEventHandler<HTMLFormElement> = async (e) => {
    e.preventDefault();
    const todo = await TodosApi.create({ content: inputText });
    setInputText("");
    setTodos([...todos, todo]);
  };

  const updateTodo = async (id: number, checked: boolean) => {
    const todo = await TodosApi.update(id, { isCompleted: checked });
    const oldTodo = todos.find((t) => t.id === id);
    if (oldTodo) {
      const index = todos.indexOf(oldTodo);
      const newTodos = [...todos];
      newTodos[index] = todo;
      setTodos(newTodos);
    }
  };

  return (
    <section className="question">
      <h2>Question 9</h2>
      <div className="question-content">
        <strong>Instructions</strong>
        <div className="question-instructions">
          This is the final and hardest question! It involves both client and
          server. You are to build me a simple todo list application, complete
          with a database. Your application should meet the following
          requirements.
          <ol>
            <li>Provide me with a input where I can type my todo item.</li>
            <li>Provide me with a save button</li>
            <li>
              When I push the save button it should make a post request to the
              server and save the todo in your database
              <ol type="a">
                <li>
                  A todo has the following shape
                  <pre>{`type Todo = {
  id: number;
  content: string;
  isCompleted: boolean;
}`}</pre>
                </li>
                <li>
                  You will need to add the model to your database and create a
                  migration for it
                </li>
              </ol>
            </li>
            <li>
              The newly created todo should be displayed in a list on the screen
            </li>
            <li>
              When the page loads you should make a get request to the server to
              get all of the todos and display them in a list
            </li>
            <li>Each todo in the list should have a checkbox</li>
            <li>
              When I check the checkbox you should make a put request to update
              the isCompleted attribute of the todo item
            </li>
          </ol>
          <div>
            The server will be running on port 8000 so you will need to make the
            request to localhost:8000 like so
            <pre>{`fetch('http://localhost:8000/todos')`}</pre>
            For the post and put requests remember to set the content type to
            application/json and to stringify the body like
            <pre>{`// for post
fetch("http://locahost:8000/todos", {
  method: "post"
  headers: {
    "Content-Type": "application/json"
  },
  body: JSON.stringify(yourPayload)
});

// for put (to update todo with id 1)
fetch("http://locahost:8000/todos/1", {
  method: "put"
  headers: {
    "Content-Type": "application/json"
  },
  body: JSON.stringify(yourPayload)
});
`}</pre>
            You will not need to worry about authentication for this.
          </div>
        </div>
        <hr />
        <div className="solution-section">
          <form onSubmit={submitTodo}>
            <input
              type="text"
              value={inputText}
              onChange={(e) => setInputText(e.target.value)}
            />
            <button>Submit</button>
          </form>
          <div>
            {todos.map((todo) => (
              <div key={todo.id}>
                <input
                  type="checkbox"
                  checked={todo.isCompleted}
                  onChange={(e) => updateTodo(todo.id, e.target.checked)}
                />{" "}
                <span>{todo.content}</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  );
};
