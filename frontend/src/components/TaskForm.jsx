import { useEffect, useState } from "react";
import Button from "./ui/Button";
import Input from "./ui/Input";
import Select from "./ui/Select";
import FormField from "./ui/FormField";
import LinkButton from "./ui/LinkButton";

function TaskForm({onSave}) {
    const [name, setName] = useState("");
    const [status, setStatus] = useState("PENDING");
    const [dueDate, setDueDate] = useState("");
    const [errors, setErrors] = useState({});
    const [sucess, setSucess] = useState("");

    useEffect(() => {
        if(!sucess) return;

        const timer = setTimeout(() => {
            setSucess("");
        },3000);

        return () => clearTimeout(timer);
    }, [sucess]);
    
    const validate = () => {
        const newErrors = {};

        if(!name.trim()) {
            newErrors.name = "Task name is required";
        }
        else if(name.trim().length < 3) {
            newErrors.name = "Task name should be more than 3 letters";
        }

        if(!dueDate) {
            newErrors.dueDate = "DueDate is required";
        }

        if(!status) {
            newErrors.status = "Status is required";
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    }

    const handleSubmit = async (e) => {
        e.preventDefault();

        if(!validate()) return;
        try {
            await onSave({
                name: name.trim(),
                dueDate,
                status
            });

            setSucess("Task created successsfully");
    
            setName("");
            setDueDate("");
            setStatus("PENDING");
            setErrors({});
        }
        catch(err) {
            setSucess("");
            console.error("Failed to create task: ",err);
        }

    }

    return(
        <div>
            <LinkButton to="/">
                Back to home
            </LinkButton><br/>
            <FormField onSubmit={handleSubmit}>
                <div>
                    <Input type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}/>
                    {errors.name && <p>{errors.name}</p>}
                </div>

                <div>
                    <Input type="date"
                    value={dueDate}
                    onChange={(e) => setDueDate(e.target.value)}/>
                    {errors.dueDate && <p>{errors.dueDate}</p>}
                </div>

                <div>
                    <Select value={status} onChange={(e) => setStatus(e.target.value)}>
                        <option value="PENDING">PENDING</option>
                        <option value="COMPLETED">COMPLETED</option>
                    </Select>
                    {errors.status && <p>{errors.status}</p>}
                </div>

                <Button type="submit">Save Task</Button>
                {sucess && <p>{sucess}</p>}
            </FormField>
        </div>
    );
}

export default TaskForm;