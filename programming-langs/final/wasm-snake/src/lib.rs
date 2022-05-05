#[macro_use]
mod utils;

use std::collections::VecDeque;
use wasm_bindgen::prelude::*;
extern crate js_sys;

// When the `wee_alloc` feature is enabled, use `wee_alloc` as the global
// allocator.
#[cfg(feature = "wee_alloc")]
#[global_allocator]
static ALLOC: wee_alloc::WeeAlloc = wee_alloc::WeeAlloc::INIT;

type Coord = (u32, u32);

#[wasm_bindgen]
#[repr(u8)]
#[derive(Clone, Copy, Debug, PartialEq, Eq)]
pub enum Cell {
    Dead = 0,
    Alive = 1,
    Apple = 2,
}

impl Cell {
    fn toggle(&mut self) {
        *self = match *self {
            Cell::Dead => Cell::Alive,
            Cell::Alive => Cell::Dead,
            Cell::Apple => Cell::Apple,
        };
    }
}

#[wasm_bindgen]
#[repr(u8)]
#[derive(Clone, Copy, Debug, PartialEq, Eq)]
pub enum Direction {
    Up = 0,
    Down = 1,
    Left = 2,
    Right = 3,
}

struct Snake {
    body: VecDeque<Coord>,
    dir: Direction,
}

impl Snake {
    pub fn new(head: Coord, tail: Coord) -> Snake {
        let mut buf = VecDeque::new();
        buf.push_back(tail);
        buf.push_back(head);
        Snake {
            body: buf,
            dir: Direction::Right,
        }
    }

    pub fn head(&mut self) -> Coord {
        return *self.body.back().expect("No head");
    }

    pub fn tail(&mut self) -> Coord {
        return *self.body.front().expect("No tail");
    }

    pub fn advance_head(&mut self) {
        let dir = self.dir;
        let head = self.head();
        let new_head = match dir {
            Direction::Right => (head.0, head.1 + 1),
            Direction::Left => (head.0, head.1 - 1),
            Direction::Down => (head.0 + 1, head.1),
            Direction::Up => (head.0 - 1, head.1),
        };

        self.body.push_back(new_head);
    }

    pub fn advance_tail(&mut self) {
        self.body.pop_front();
    }
}

#[wasm_bindgen]
pub struct Universe {
    width: u32,
    height: u32,
    snake: Snake,
    cells: Vec<Cell>,
    score: u8,
}

impl Universe {
    fn get_index(&self, row: u32, column: u32) -> usize {
        (row * self.width + column) as usize
    }

    /// Get the dead and alive values of the entire universe.
    pub fn get_cells(&self) -> &[Cell] {
        &self.cells
    }

    /// Set cells to be alive in a universe by passing the row and column
    /// of each cell as an array.
    pub fn set_cells(&mut self, cells: &[Coord]) {
        for (row, col) in cells.iter().cloned() {
            let idx = self.get_index(row, col);
            self.cells[idx] = Cell::Alive;
        }
    }

    pub fn toggle_cell(&mut self, row: u32, column: u32) {
        let idx = self.get_index(row, column);
        self.cells[idx].toggle();
    }
}

#[wasm_bindgen]
impl Universe {
    pub fn new(width: u32, height: u32) -> Universe {
        utils::set_panic_hook();

        let cells = vec![Cell::Dead; (width * height) as usize];

        let snake = Snake::new((0, 1), (0, 0));

        let mut un = Universe {
            width,
            height,
            snake,
            cells,
            score: 0,
        };

        un.toggle_cell(0, 0);
        un.toggle_cell(0, 1);
        un.place_random_apple();

        un
    }

    pub fn width(&self) -> u32 {
        self.width
    }

    pub fn height(&self) -> u32 {
        self.height
    }

    pub fn score(&self) -> u8 {
        self.score
    }

    pub fn cells(&self) -> *const Cell {
        self.cells.as_ptr()
    }

    pub fn set_width(&mut self, width: u32) {
        self.width = width;
        self.cells = (0..width * self.height).map(|_i| Cell::Dead).collect();
    }

    pub fn set_height(&mut self, height: u32) {
        self.height = height;
        self.cells = (0..self.width * height).map(|_i| Cell::Dead).collect();
    }

    pub fn restart(&mut self) {
        self.cells = (0..self.width * self.height).map(|_i| Cell::Dead).collect();
        self.toggle_cell(0, 0);
        self.toggle_cell(0, 1);
        self.place_random_apple();
        self.score = 0;
        self.snake = Snake::new((0, 1), (0, 0)); // Does this clean up the old snake?
    }

    pub fn set_direction(&mut self, dir: Direction) {
        let current_dir = self.snake.dir;
        if (dir == Direction::Down && current_dir != Direction::Up)
            || (dir == Direction::Up && current_dir != Direction::Down)
            || (dir == Direction::Left && current_dir != Direction::Right)
            || (dir == Direction::Right && current_dir != Direction::Left)
        {
            self.snake.dir = dir;
        }
    }

    pub fn place_apple(&mut self, row: u32, column: u32) {
        let index = self.get_index(row, column);
        self.cells[index] = Cell::Apple;
    }

    pub fn place_random_apple(&mut self) {
        let mut index: usize =
            (js_sys::Math::random() * (self.height() * self.width()) as f64) as usize;
        while self.cells[index] == Cell::Alive {
            index = (js_sys::Math::random() * (self.height() * self.width()) as f64) as usize;
        }
        self.cells[index] = Cell::Apple;
    }

    pub fn tick(&mut self) -> bool {
        self.snake.advance_head();

        let (row, column) = self.snake.head();
        if (row >= self.height) || (column >= self.width) {
            log!("{}, {}", row, column);
            return false;
        };
        let index = self.get_index(row, column);

        match self.cells[index] {
            Cell::Apple => {
                self.cells[index] = Cell::Alive;
                self.score += 1;
                self.place_random_apple();
            }
            Cell::Dead => {
                self.cells[index] = Cell::Alive;
                let (row, column) = self.snake.tail();
                let index = self.get_index(row, column);
                self.cells[index] = Cell::Dead;
                self.snake.advance_tail();
            }
            Cell::Alive => return false,
        };

        return true;
    }
}
