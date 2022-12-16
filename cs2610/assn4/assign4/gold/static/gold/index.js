window.addEventListener("DOMContentLoaded", event => {
  let goldValue = 0;
  getGoldValue().then(value => {
    goldValue = value;
    const element = document.getElementById("goldValue");
    element.textContent = `Current Gold Value: $${goldValue} per Troy Ounce`;

    const button = document.getElementById("submit");
    button.disabled = false;
  });

  const form = document.getElementById("calculate");
  form.onsubmit = event => {
    event.preventDefault();
    const errorElement = document.getElementById("error");
    const weightString = document.getElementById("weight").value;
    if (!isNumeric(weightString)) {
      errorElement.textContent = "Invalid weight";
      return;
    } else {
      errorElement.textContent = "";
      const weight = parseFloat(weightString);
      const unit = document.getElementById("unit").value;
      convertToTroyOunces(weight, unit).then(value => {
        console.log(value);
        addResult(`$${(value * goldValue).toFixed(2)}`, "#222");
      });
    }
  };
});

function getGoldValue() {
  const API_KEY = "mkAukvs2JRPnW637V3By";
  const start = new Date();
  start.setDate(start.getDate() - 5);
  const startString = start.toISOString().split("T")[0];

  const end = new Date();
  const endString = end.toISOString().split("T")[0];

  return fetch(
    `https://www.quandl.com/api/v3/datasets/LBMA/GOLD?start_date=${startString}&end_date=${endString}&column_index=1api_key=${API_KEY}`
  )
    .then(response => response.json())
    .then(data => data.dataset.data[0][1]);
}

function convertToTroyOunces(value, unit) {
  return fetch(`/unitconv/convert?from=${unit}&to=t_oz&value=${value}`)
    .then(response => response.json())
    .then(data => data.value);
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

//https://stackoverflow.com/questions/175739/built-in-way-in-javascript-to-check-if-a-string-is-a-valid-number
function isNumeric(str) {
  if (typeof str != "string") return false;
  return !isNaN(str) && !isNaN(parseFloat(str));
}
