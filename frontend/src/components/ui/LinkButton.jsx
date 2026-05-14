import { Link } from "react-router-dom";

function LinkButton({to, children}) {
    return (
        <Link to={to} className="link-button">
            {children}
        </Link>
    )
}

export default LinkButton;