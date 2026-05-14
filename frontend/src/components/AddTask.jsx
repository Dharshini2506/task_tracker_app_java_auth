import TaskForm from "./TaskForm";

function AddTask({fetchTasks}) {
    const handleCreate = async (taskData) => {
        const res = await fetch("http://localhost:8080/tasks/create", {
            method : "POST",
            headers : {
                "Content-Type" : "application/json"
            },
            body: JSON.stringify(taskData)
        });

        if(!res.ok) {
            throw new Error("Failes to create task");
        }

        fetchTasks();
        return res;
    };

    return <TaskForm onSave = {handleCreate}/>
}

export default AddTask;