function Input({type="text", ...props}) {
    return <input type={type} {...props} className="input"/>
}

export default Input;