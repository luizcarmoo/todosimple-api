const url = "http://localhost:8080/task/user/1";


function hideLoader() {
  document.getElementById("loading").style.display = "none";
}


function show(tasks) {


  let tab = `
    <thead>
    <th scope="User Id">Id</th>
    <th scope="User Name">User Name</th>
    <th scope="col">Task</th>
      <th scope="Description">Description</th>
    </thead>`;
 
    for (let task of tasks) {
        tab += `
          <tr>
          <td>${task.user.id}</td>
          <td>${task.user.username}</td>
          <td scope="row">${task.id}</td>
            <td>${task.description}</td>
          </tr>
        `;
       
      }


      document.getElementById("tasks").innerHTML = tab;


  }


  async function getApi(url) {
    const response = await fetch(url, { method: "GET" });


    var data = await response.json();
    console.log(data);
    
    if(response) hideLoader();
    show(data);
  }

  getApi(url);