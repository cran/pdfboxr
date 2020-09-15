# **pdfboxr**

The **R** package **pdfboxr** provides an interface to the [Apache PDFBox](https://pdfbox.apache.org/) library.

## Installation
```{r}
remotes::install_gitlab("schwe/pdfboxr")
```

## Basic usage
```{r}
library("pdfboxr")

args(read_chars)
```

```
#R> function (file, pages = integer(), adjust = TRUE, size_pt = FALSE, 
#R>     digits = 2L, password = "", max_memory = -1L, temp_dir = "") 
#R> NULL
```

```{r}
pdf_file <- system.file("pdfs/cars.pdf", package = "pdfboxr")
d <- read_chars(pdf_file)
d
```

```
#R> A pdf document with 2 pages and
#R>   metainfo text fonts
#R> 1        2  313     3
#R> elements.
```

The function `read.pdf()` returns an object of class `pdf_document`
(a list containing `data.frame`'s). The `pdf_document` object contains the elements:

- `"metainfo"`
- `"text"`
- `"fonts"`

The elements can be accessed as by each other list.
```{r}
head(d[["text"]])
```

```
#R>   pid text                  font size     x0     y0     x1    y1 width height yscale xscale rotation
#R> 1   1    c BAAAAA+LiberationSans   10 288.20 769.12 293.20 774.7  5.00   5.58     10     10        0
#R> 2   1    a BAAAAA+LiberationSans   10 293.20 769.12 298.76 774.7  5.56   5.58     10     10        0
#R> 3   1    r BAAAAA+LiberationSans   10 298.79 769.12 302.12 774.7  3.33   5.58     10     10        0
#R> 4   1    s BAAAAA+LiberationSans   10 302.09 769.12 307.09 774.7  5.00   5.58     10     10        0
#R> 5   1    s          Courier-Bold   12  77.20 747.29  84.40 753.6  7.20   6.31     12     12        0
#R> 6   1    p          Courier-Bold   12  84.40 747.29  91.60 753.6  7.20   6.31     12     12        0
``` 

The **R** package **pdfboxr** only returns raw data extracted from the
**PDF**-file. To refine this raw data into a format usable for data analysis
the **pdfmole** can be used.
