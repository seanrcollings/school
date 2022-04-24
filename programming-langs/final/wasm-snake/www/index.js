import { Universe, Cell, Direction } from "wasm-snake";
import { memory } from "wasm-snake/wasm_snake_bg";

const CELL_SIZE = 20; // px
const GRID_COLOR = "#CCCCCC";
const cellColors = {
  [Cell.Dead]: "#FFFFFF",
  [Cell.Alive]: "#42f560",
  [Cell.Apple]: "#FF0000"
}

const universe = Universe.new(32, 32);
const width = universe.width();
const height = universe.height();

/** @type {HTMLCanvasElement} */
const canvas = document.getElementById("game-of-life-canvas");
canvas.height = (CELL_SIZE + 1) * height + 1;
canvas.width = (CELL_SIZE + 1) * width + 1;
/** @type {CanvasRenderingContext2D} */
const ctx = canvas.getContext('2d');


const getIndex = (row, column) => {
  return row * width + column;
};

const drawGrid = () => {
  ctx.beginPath();
  ctx.strokeStyle = GRID_COLOR;

  // Vertical lines.
  for (let i = 0; i <= width; i++) {
    ctx.moveTo(i * (CELL_SIZE + 1) + 1, 0);
    ctx.lineTo(i * (CELL_SIZE + 1) + 1, (CELL_SIZE + 1) * height + 1);
  }

  // Horizontal lines.
  for (let j = 0; j <= height; j++) {
    ctx.moveTo(0,                           j * (CELL_SIZE + 1) + 1);
    ctx.lineTo((CELL_SIZE + 1) * width + 1, j * (CELL_SIZE + 1) + 1);
  }

  ctx.stroke();
}

const drawCells = () => {
  const cellsPtr = universe.cells();
  const cells = new Uint8Array(memory.buffer, cellsPtr, width * height);

  ctx.beginPath();

  for (let row = 0; row < height; row++) {
    for (let col = 0; col < width; col++) {
      const idx = getIndex(row, col);

      ctx.fillStyle = cellColors[cells[idx]];


      ctx.fillRect(
        col * (CELL_SIZE + 1) + 1,
        row * (CELL_SIZE + 1) + 1,
        CELL_SIZE,
        CELL_SIZE
      );
    }
  }

  ctx.stroke();
};

const keys = {
  "ArrowUp": Direction.Up,
  "ArrowDown": Direction.Down,
  "ArrowLeft": Direction.Left,
  "ArrowRight": Direction.Right,
};

document.addEventListener("keydown", (e) => {
  if (e.key in keys) {
    universe.set_direction(keys[e.key]);
  }
})

const score = document.getElementsByClassName("score");
const results = document.getElementById("results-screen");
const restart = document.getElementById("restart");
restart.addEventListener("click", () => {
  results.style.opacity = "0";
  universe.restart();
  renderLoop();
})

const renderLoop = () => {
  const alive = universe.tick();

  for (let i = 0; i < score.length; i++) {
    score[i].innerHTML = universe.score();
  }

  if (alive) {
    drawGrid();
    drawCells();
    setTimeout(() => requestAnimationFrame(renderLoop), Math.log((100) / (universe.score() + 1)) * 20)
  } else {
    results.style.opacity = "1";
  }
}


drawGrid();
drawCells();
renderLoop();
