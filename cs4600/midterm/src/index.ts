import express from "express";
import { PrismaClient } from "@prisma/client";
import dotenv from "dotenv";
import cors from "cors";
dotenv.config();

const client = new PrismaClient();
const app = express();
app.use(express.json());
app.use(cors()); // need for cross origin requests

interface CreateTodo {
  content: string;
  isCompleted?: boolean;
}

interface UpdateTodo {
  isCompleted: boolean;
}

app.post("/todos", async (req, res) => {
  const { content, isCompleted = false } = req.body as CreateTodo;
  const todo = await client.todo.create({ data: { content, isCompleted } });
  res.json({ todo });
});

app.get("/todos", async (req, res) => {
  const todos = await client.todo.findMany();
  res.json({ todos });
});

app.put("/todos/:id", async (req, res) => {
  const id = parseInt(req.params.id, 10);
  const { isCompleted } = req.body as UpdateTodo;
  const todo = await client.todo.update({
    where: { id },
    data: { isCompleted },
  });
  res.json({ todo });
});

app.listen(process.env.PORT, () => {
  console.log(`Listening on port ${process.env.PORT}`);
});
