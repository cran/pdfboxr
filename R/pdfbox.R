#' @noRd
#' @export
print.pdf_document <- function(x, ...) {
    n_pages <- max(x$metainfo$pid)
    if ( n_pages == 1L ) {
        writeLines(sprintf("A pdf document with %s page and", n_pages))
    } else {
        writeLines(sprintf("A pdf document with %s pages and", n_pages))
    }
    print(data.frame(lapply(x, nrow)))
    writeLines("elements.")
}

from_vlist <- function(x, digits = 2L) {
    keys <- x$names()
    y <- vector("list", length(keys))
    for (i in seq_along(keys)) {
        z <- x$get(keys[i])$toR()
        y[[i]] <- if (is.double(z)) round(z, digits) else z
    }
    names(y) <- keys
    y
}


#' @title Read Characters from PDF
#' @description Extract all the characters of a PDF file with information about the position,
#'              font, rotation, ....
#' @param file path to PDF file (is auto-expanded with [path.expand()])
#' @param pages an \code{integer} vector giving the pages which should be extracted 
#'      (default is \code{integer()}).
#' @param adjust a \code{logical} if \code{TRUE} the variables \code{"x0"}, \code{"y0"},
#'      \code{"height"} and \code{"width"} are direction adjusted. 
#' @param size_pt a \code{logical} giving if the font size should be returned in \code{pt}.
#' @param digits an \code{integer} (of length 1) giving to how many digits the
#'      \code{double} variables (e.g. \code{"x0"}, \code{"y1"}) should be rounded
#'      (the default is \code{2L} and this should be enough).
#' @param password a string providing the password of the file.
#' @param max_memory an integer giving the maximum number of main-memory in MB 
#'      to be used by \pkg{pdfbox}. The default is \code{-1L} which means 
#'      there is no limit. If a limit is set \pkg{pdfbox} will try to stay
#'      below by performing out of memory computations.
#'      Since the memory of the \code{Java} virtual machine
#'      is already limited it is recommended to choose the value of \code{max\_memory}
#'      below the memory limit of the virtual machine (\code{options("java.parameters")}).
#'      If the memory of the \code{Java} virtual machine is big enough 
#'      this options is never needed.
#' @param temp_dir a character string giving the path to a temporary directory.
#' @examples
#' pdf_file <- system.file("pdfs/cars.pdf", package = "pdfboxr")
#' pdf <- read_chars(pdf_file, 2L)
#' pdf
#' @return Returns a object of class \code{"pdf_document"}.
#' @export
read_chars <- function(file, pages=integer(), adjust=TRUE, size_pt=FALSE, digits=2L, password="",
    max_memory = -1L, temp_dir = tempdir()) {

    assert(check_character(file, any.missing=FALSE, len=1L), check_file_exists(file), 
        check_integerish(pages, any.missing=FALSE), check_logical(adjust, any.missing=FALSE, len=1L), 
        check_logical(size_pt, any.missing=FALSE, len=1L), check_integerish(digits, any.missing=FALSE, len=1L),
        check_character(password, any.missing=FALSE, len=1L), check_integerish(max_memory, any.missing=FALSE, len=1L),
        check_character(temp_dir, any.missing=FALSE, len=1L), combine = "and")

    pdf <- J("interfaces.PdfExtractor")
    pdf$set_memory_options(as.integer(max_memory), temp_dir)
    pdf$load(file, password)
    if ( length(pages) == 0L ) {
        pages <- seq_len(pdf$number_of_pages())
    }

    pdf$read_chars(.jarray(pages), if (isTRUE(adjust)) "true" else "false", if (isTRUE(size_pt)) "true" else "false")
    df <- as.data.frame(from_vlist(pdf$df), stringsAsFactors = FALSE)
    metainfo <- as.data.frame(from_vlist(pdf$meta), stringsAsFactors = FALSE)
    pdf$close_document()
    rm(pdf)

    fonts <- unique(df[,c('font', 'size', 'space_width')])
    df$space_width <- NULL
    cnames <- c("pid", "text", "font", "size", "x0", "y0", "x1", "y1", 
                "width", "height", "yscale", "xscale", "rotation")    
    df <- df[, cnames]
    df$y0 <- df$y1 - df$height
    dat <- list(metainfo=metainfo, text=df, fonts=fonts)
    class(dat) <- "pdf_document"
    dat
}


#' @title Read Text from PDF
#' @description Read text from a PDF file.
#' @param file path to PDF file (is auto-expanded with [path.expand()])
#' @param pages an \code{integer} vector giving the pages which should be extracted 
#'      (default is \code{integer()}).
#' @param password a string providing the password of the file.
#' @param max_memory an integer giving the maximum number of main-memory in MB 
#'      to be used by \pkg{pdfbox}. The default is \code{-1L} which means 
#'      there is no limit. If a limit is set \pkg{pdfbox} will try to stay
#'      below by performing out of memory computations.
#'      Since the memory of the \code{Java} virtual machine
#'      is already limited it is recommended to choose the value of \code{max\_memory}
#'      below the memory limit of the virtual machine (\code{options("java.parameters")}).
#'      If the memory of the \code{Java} virtual machine is big enough 
#'      this options is never needed.
#' @param temp_dir a character string giving the path to a temporary directory.
#' @examples
#' pdf_file <- system.file("pdfs/cars.pdf", package = "pdfboxr")
#' pdf <- read_text(pdf_file)
#' pdf
#' @return Returns a object of class \code{"data.frame"}.
#' @export
read_text <- function(file, pages=integer(), password="", max_memory = -1L, temp_dir = tempdir()) {

    assert(check_character(file, any.missing=FALSE, len=1L), check_file_exists(file), 
        check_integerish(pages, any.missing=FALSE), check_character(password, any.missing=FALSE, len=1L), 
        check_integerish(max_memory, any.missing=FALSE, len=1L), 
        check_character(temp_dir, any.missing=FALSE, len=1L), combine = "and")

    pdf <- J("interfaces.PdfExtractor")
    pdf$set_memory_options(as.integer(max_memory), temp_dir)
    pdf$load(file, password)
    if ( length(pages) == 0L ) {
        pages <- seq_len(pdf$number_of_pages())
    }

    pdf$read_text(.jarray(pages))
    dat <- as.data.frame(from_vlist(pdf$df), stringsAsFactors = FALSE)
    pdf$close_document()
    rm(pdf)

    return(dat)
}
