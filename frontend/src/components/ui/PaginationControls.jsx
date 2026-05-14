import Button from "./Button";

function PaginationControls({currentPage, totalPages, onPrev, onNext}) {
    return (
        <div>
            <Button onClick={onPrev} disabled={currentPage === 1}>Prev</Button>
            <span>Page {currentPage} of {totalPages}</span>
            <Button onClick={onNext} disabled={currentPage === totalPages}>Next</Button>
        </div>
    )
}

export default PaginationControls;