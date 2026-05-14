import Button from "./ui/Button";
import Input from "./ui/Input";
import Select from "./ui/Select";
import PaginationControls from "./ui/PaginationControls";
import Modal from "./ui/Modal";
import useTaskList from "./customHooks/useTaskList";
import LinkButton from "./ui/LinkButton";

function TaskList({ tasks, fetchTasks }) {
  const task = useTaskList(tasks, fetchTasks);

  return (
    <div className="app-container">
      <LinkButton to="/add">
        Add Tasks
      </LinkButton><br/>
      <div className="task-search">
        <Input type="text"
          placeholder="Search fields ..." 
          value={task.searchField}
          onChange={(e) => { 
            task.setSearchField(e.target.value);
            task.setCurrentPage(1);
          }}
          className="input"/>

          <Select value={task.statusFilter} onChange={(e) => {
            task.setStatusFilter(e.target.value);
            task.setCurrentPage(1);
          }} className="select">
            <option value="ALL">ALL</option>
            <option value="PENDING">PENDING</option>
            <option value="COMPLETED">COMPLETED</option>
          </Select>

          <Input type="date"
            value={task.dateFilter}
            onChange={(e) => {
              task.setDateFilter(e.target.value);
              task.setCurrentPage(1);
            }} className="input"/>
            <Button onClick={task.clearFilters}>Clear Filters</Button>
      </div>
      <div className="task-search">
        <Select value={task.sortField} onChange={(e) => task.setSortField(e.target.value)} className="select">
          <option value="name">Name</option>
          <option value="dueDate">DueDate</option>
          <option value="status">Status</option>
        </Select>
        <Select value={task.sortOrder} onChange={(e) => task.setSortOrder(e.target.value)} className="select">
          <option value="asc">Ascending</option>
          <option value="desc">Descending</option>
        </Select>
      </div>
      {task.currentTasks.map((t) => (
        <div key={t.id} className="task-card">
          
          {task.editTaskId === t.id ? (
            <>
              <Input type="text" name="name" value={task.editTasks.name} onChange={task.handleChange} className="input"/>
              <Input type="date" name="dueDate" value={task.editTasks.dueDate} onChange={task.handleChange} className="input"/>
              <Select name="status" value={task.editTasks.status} onChange={task.handleChange} className="select" disabled={t.status === "COMPLETED"}>
                <option value="PENDING">PENDING</option>
                <option value="COMPLETED">COMPLETED</option>
              </Select>
              <br/>
              <Button onClick={() => task.handleSave(t.id)} className="btn">SAVE</Button>
            </>
          ) : (
            <div>
              <div className="task-info">
                <span className="task-name">{t.name}</span>
                <span className="task-meta">Due: {t.dueDate}</span> 
                <span className={`status-badge ${t.status === "COMPLETED" ? "status-completed" : "status-pending"}`}>{t.status}</span>
              </div><br/>
              <div className="task-actions">
                <Button onClick={() => task.handleEdit(t)} className="btn">EDIT</Button>
                <Button onClick={() => {
                  task.setSelectedTaskId(t.id);
                  task.setShowModal(true);
                }} className="btn-danger">DELETE</Button>
                </div>
              </div>
          )}
        </div>
      ))}
      <PaginationControls 
        currentPage={task.currentPage} 
        totalPages={task.totalPages} 
        onPrev={() => task.setCurrentPage(task.currentPage - 1)}
        onNext={() => task.setCurrentPage(task.currentPage + 1)}/>

      <Modal isOpen={task.showModal} onClose={() => task.setShowModal(true)}>
        <p>Are you sure you want to delete?</p>
        <Button onClick={() => {
          task.handleDelete(task.selectedTaskId); 
          task.setShowModal(false);
        }}>YES</Button>
        <Button onClick={() => task.setShowModal(false)}>
          NO
        </Button>
      </Modal>
    </div>
  );
}

export default TaskList;
