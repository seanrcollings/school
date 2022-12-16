window.addEventListener("DOMContentLoaded", event => {
  // Call all the setup nessecary
  document.title = "JavaScript Calculator";
  const body = document.getElementsByTagName("BODY")[0];
  buildCalculator(body);
  setupResults(body);
});

/* Setsup the Calculator Block */
function buildCalculator(target) {
  const calculatorDiv = document.createElement("DIV");
  calculatorDiv.className = "calculator";

  const header = document.createElement("h1");
  header.textContent = "JavaScript Calculator";

  const inputs = createInputs();
  const form = document.createElement("FORM");
  form.appendChild(inputs);
  form.onsubmit = event => {
    event.preventDefault();
    calculate();
  };

  appendChildren(calculatorDiv, header, form);
  target.appendChild(calculatorDiv);
}

function createInputs() {
  const inputsContainer = document.createElement("DIV");
  const input1 = createOperand("operand1");
  const operatorSelect = createSelect("operator", [
    "+",
    "-",
    "/",
    "*",
    "%",
    "**",
  ]);
  const input2 = createOperand("operand2");
  const colorPicker = document.createElement("input");
  colorPicker.type = "color";
  colorPicker.id = "color";

  const submit = createSubmit();

  appendChildren(
    inputsContainer,
    input1,
    operatorSelect,
    input2,
    colorPicker,
    submit
  );
  return inputsContainer;
}

function createOperand(id, placeholder = 4) {
  const operand = document.createElement("INPUT");
  operand.type = "number";
  operand.id = id;
  operand.placeholder = placeholder;
  return operand;
}

function createSelect(id, options) {
  const select = document.createElement("SELECT");
  select.id = id;
  options.forEach(option => {
    const optionElement = document.createElement("option");
    optionElement.text = option;
    select.add(optionElement);
  });
  return select;
}

function createSubmit() {
  const compute = document.createElement("BUTTON");
  compute.textContent = "Compute";
  return compute;
}

/* Creates the Results Block */
function setupResults(target) {
  const results = document.createElement("DIV");
  results.id = "results";
  target.appendChild(results);
}

/*  preforms the calculation and calls addResult
to add the final result to the results div
 */
function calculate() {
  const operand1 = document.getElementById("operand1");
  const operand2 = document.getElementById("operand2");
  const operator = document.getElementById("operator");
  const colorPicker = document.getElementById("color");
  try {
    const solution = eval(
      `${operand1.value}${operator.value}${operand2.value}`
    );
    addResult(
      `${operand1.value} ${operator.value} ${operand2.value} = ${solution}`,
      colorPicker.value
    );
  } catch (e) {
    addResult("Missing one or both operands!", "#F22");
  }
}

function addResult(text, color) {
  const result = document.createElement("DIV");
  result.setAttribute("style", `background-color: ${color}`);
  result.className = "result shadowed";
  const results = document.getElementById("results");
  results.insertBefore(result, results.firstChild);

  result.onclick = () => {
    results.removeChild(result);
  };

  const timeStamp = document.createElement("SPAN");
  timeStamp.textContent = new Date().toLocaleString();
  timeStamp.className = "timestamp";

  const textElement = document.createElement("SPAN");
  textElement.textContent = text;

  appendChildren(result, timeStamp, textElement);
}

// Utils functions

function appendChildren(parent, ...children) {
  for (i = 0; i < children.length; i++) {
    parent.appendChild(children[i]);
  }
}
