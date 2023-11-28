import { Observable, from, forkJoin, of } from 'rxjs';
import { map, mergeMap } from 'rxjs/operators';

static splitFileByPages(ocrRequest: OcrRequest): Observable<Blob[]> {
  const pagesize = Number(apiconfig.ocrRequestPagesize);

  return from(new Blob([ocrRequest.file]).arrayBuffer()).pipe(
    mergeMap((requestFileBuffer) => from(PDFDocument.load(requestFileBuffer, { updateMetadata: false }))),
    map((requestPdfDocument) => {
      const requestFileBlobs: Blob[] = [];
      let newPdfDocument = PDFDocument.create();

      for (let i = 0; i <= requestPdfDocument.getPageCount() - 1; i++) {
        newPdfDocument.setCreator(requestPdfDocument.getCreator());

        const [ocrPage] = newPdfDocument.copyPages(requestPdfDocument, [i]);
        newPdfDocument.addPage(ocrPage);

        if (i < pagesize && i < requestPdfDocument.getPageCount() - 1) {
          continue;
        }

        const ocrReqPdfBlob = new Blob([newPdfDocument.save()], { type: DocumentocrMapper.APPLICATION_PDF });
        requestFileBlobs.push(ocrReqPdfBlob);

        pagesize = i + Number(apiconfig.ocrRequestPageSize);
        newPdfDocument = PDFDocument.create();
      }

      return requestFileBlobs;
    }),
    forkJoin()
  );
}
