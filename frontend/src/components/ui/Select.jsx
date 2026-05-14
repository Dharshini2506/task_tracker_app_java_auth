function Select({children, ...props}) {
    return <select {...props} className="select">{children}</select>
}

export default Select;