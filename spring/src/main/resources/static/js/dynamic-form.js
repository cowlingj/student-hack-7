
function addInput() {

    var form = document.querySelector("#form")

    var id = document.createElement("input")

    id.setAttribute("type", "number")
    id.setAttribute("value", "10")

    var review = document.createElement("input")

    review.setAttribute("type", "number")
    review.setAttribute("value", "9")

    form.appendChild(id)
    form.appendChild(review)
}