library("pdfboxr")

test__read_chars <- function() {
    pdffile <- system.file("pdfs/cars.pdf", package = "pdfboxr")
    pdf <- read_chars(pdffile)
    stopifnot(inherits(pdf, "pdf_document"))
}

test__read_text <- function() {
    pdffile <- system.file("pdfs/cars.pdf", package = "pdfboxr")
    pdf <- read_text(pdffile)
    stopifnot(inherits(pdf, "data.frame"))
}

test__read_chars()
test__read_text()
