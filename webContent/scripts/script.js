function logout() {
  localStorage.removeItem('Authorization');
  window.location = '../login_page/login.html';
}

document.addEventListener("DOMContentLoaded", function () {
  const tasksEndpoint = "http://localhost:8080/task/user";

  function hideLoader() {
    document.getElementById("loading").style.display = "none";
  }

  function show(tasks) {
    let tab = `<thead>
              <th scope="col">#</th>
              <th scope="col">Description</th>
          </thead>`;

    for (let task of tasks) {
      tab += `
              <tr id="task-row-${task.id}">
                  <td scope="row">${task.id}</td>
                  <td>${task.description}</td>
              </tr>
          `;
    }

    document.getElementById("tasks").innerHTML = tab;
  }

  async function getTasks() {
    let key = "Authorization";
    const response = await fetch(tasksEndpoint, {
      method: "GET",
      headers: new Headers({
        Authorization: localStorage.getItem(key),
      }),
    });

    var data = await response.json();
    console.log(data);
    if (response) hideLoader();
    show(data);
    deleteButton(data);
  }

  function deleteButton(tasks) {
    tasks.forEach(task => {
      const deleteButton = document.createElement("button");
      deleteButton.className = "btn btn-danger btn-sm";
      deleteButton.innerHTML = "&#x2715;";
      deleteButton.onclick = function() { deleteTask(task.id); };
      
      const taskRow = document.getElementById(`task-row-${task.id}`);
      const actionCell = document.createElement("td");
      actionCell.appendChild(deleteButton);
      taskRow.appendChild(actionCell);
    });
  }

  async function deleteTask(taskId) {
    let key = "Authorization";
    
    const response = await fetch(`http://localhost:8080/task/${taskId}`, {
      method: "DELETE",
      headers: new Headers({
        Authorization: localStorage.getItem(key),
      }),
    });

    if (response.ok) {
      getTasks(); 
    } else {
      alert("Error deleting task.");
    }
  }

  document.getElementById("taskForm").addEventListener("submit", async function (event) {
    event.preventDefault();

    let key = "Authorization";
    const taskDescription = document.getElementById("taskDescription").value;

    const response = await fetch("http://localhost:8080/task", {
      method: "POST",
      headers: new Headers({
        "Content-Type": "application/json; charset=utf8",
        Accept: "application/json",
        Authorization: localStorage.getItem(key),
      }),
      body: JSON.stringify({
        description: taskDescription,
      }),
    });

    if (response.ok) {
      let taskModal = bootstrap.Modal.getInstance(document.getElementById("taskModal"));
      taskModal.hide();
      getTasks(); 
    } else {
      alert("Error adding task.");
    }
  });

  if (!localStorage.getItem("Authorization"))
    window.location = "/webContent/home_page/index.html";

  getTasks();
});
