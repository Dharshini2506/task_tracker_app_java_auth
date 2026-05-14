import { useState } from "react";

function useTaskList(tasks, fetchTasks) {
  const [editTaskId, setEditTaskId] = useState(null);
  const [editTasks, setEditTasks] = useState({
    name: "",
    dueDate: "",
    status: ""
  });

  const [showModal, setShowModal] = useState(false);
  const [selectedTaskId, setSelectedTaskId] = useState(null);

  const [searchField, setSearchField] = useState("");
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [dateFilter, setDateFilter] = useState("");

  const [sortField, setSortField] = useState("name");
  const [sortOrder, setSortOrder] = useState("asc");

  const [currentPage, setCurrentPage] = useState(1);
  const tasksPerPage = 5;

  const filteredTasks = tasks.filter(task => {
    const matchedName =
      searchField === "" ||
      task.name.toLowerCase().includes(searchField.toLowerCase());

    const matchedStatus =
      statusFilter === "ALL" || task.status === statusFilter;

    const matchedDate =
      dateFilter === "" || task.dueDate === dateFilter;

    return matchedName && matchedStatus && matchedDate;
  });

  const sortedTasks = [...filteredTasks].sort((a, b) => {
    if (a[sortField] < b[sortField]) return sortOrder === "asc" ? -1 : 1;
    if (a[sortField] > b[sortField]) return sortOrder === "asc" ? 1 : -1;
    return 0;
  });

  const totalPages = Math.ceil(sortedTasks.length / tasksPerPage);
  const lastTaskIndex = currentPage * tasksPerPage;
  const firstTaskIndex = lastTaskIndex - tasksPerPage;
  const currentTasks = sortedTasks.slice(firstTaskIndex, lastTaskIndex);

  const clearFilters = () => {
    setSearchField("");
    setStatusFilter("ALL");
    setDateFilter("");
    setCurrentPage(1);
  };

  const handleEdit = (task) => {
    setEditTaskId(task.id);
    setEditTasks({
      name: task.name,
      dueDate: task.dueDate,
      status: task.status
    });
  };

  const handleChange = (e) => {
    setEditTasks({
      ...editTasks,
      [e.target.name]: e.target.value
    });
  };

  const handleSave = async (id) => {
    await fetch(`http://localhost:8080/tasks/edit/${id}`, {
      method: "PATCH",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(editTasks)
    });

    setEditTaskId(null);
    fetchTasks();
  };

  const handleDelete = async () => {
    await fetch(`http://localhost:8080/tasks/delete/${selectedTaskId}`, {
      method: "DELETE"
    });

    setShowModal(false);
    fetchTasks();
  };

  return {
    currentTasks,
    currentPage,
    totalPages,
    editTaskId,
    editTasks,
    showModal,

    searchField,
    statusFilter,
    dateFilter,
    sortField,
    sortOrder,

    setSearchField,
    setStatusFilter,
    setDateFilter,
    setSortField,
    setSortOrder,
    setCurrentPage,
    setShowModal,
    setSelectedTaskId,

    clearFilters,
    handleEdit,
    handleChange,
    handleSave,
    handleDelete
  };
}

export default useTaskList;
