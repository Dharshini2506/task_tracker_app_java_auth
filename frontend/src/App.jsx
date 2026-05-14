import { useEffect, useState } from "react"
import AddTask from "./components/AddTask"
import TaskList from "./components/TaskList"
import { Route, Routes } from "react-router-dom";
import "./styles/layout.css";
import "./styles/ui.css";

function App() {
  const [tasks, setTasks] = useState([]);

  const fetchTasks = async () => {
    const response = await fetch("http://localhost:8080/tasks/viewAll");
    const result = await response.json();
    setTasks(result.data);
  }

  useEffect(() => {
    fetchTasks();
  }, []);

  return (
    <>
      <h1 style={{  textAlign: "center" }}>Experiment 1 - Task tracker app</h1>
      <Routes>
        <Route path="/" 
        element = {<TaskList tasks={tasks} fetchTasks={fetchTasks}/>}/>
        <Route path="/add"
        element = {<AddTask fetchTasks={fetchTasks}/>}/>
      </Routes>
    </>
  );
}

export default App;
