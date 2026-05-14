function FormField({label, children, error, onSubmit}) {
    return (
        <form onSubmit={onSubmit}>
            {label && <label>{label}</label>}
            {children}
            {error && <p>{error}</p>}
        </form>
    )
}

export default FormField;