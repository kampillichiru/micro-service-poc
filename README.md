import { Observable, from, forkJoin } from 'rxjs';
import { map, mergeMap } from 'rxjs/operators';

static splitFileByPages(ocrRequest: OcrRequest): Observable<Blob[]> {
  const pagesize = Number(apiconfig.ocrRequestPagesize);

  return from(new Blob([ocrRequest.file]).arrayBuffer()).pipe(
    mergeMap(async (requestFileBuffer) => await PDFDocument.load(requestFileBuffer, { updateMetadata: false })),
    map(async (requestPdfDocument) => {
      const requestFileBlobs: Blob[] = [];
      let newPdfDocument = await PDFDocument.create();

      for (let i = 0; i <= requestPdfDocument.getPageCount() - 1; i++) {
        newPdfDocument.setCreator(requestPdfDocument.getCreator());

        const [ocrPage] = await newPdfDocument.copyPages(requestPdfDocument, [i]);
        newPdfDocument.addPage(ocrPage);

        if (i < pagesize && i < requestPdfDocument.getPageCount() - 1) {
          continue;
        }

        const ocrReqPdfBlob = new Blob([await newPdfDocument.save()], { type: DocumentocrMapper.APPLICATION_PDF });
        requestFileBlobs.push(ocrReqPdfBlob);

        pagesize = i + Number(apiconfig.ocrRequestPageSize);
        newPdfDocument = await PDFDocument.create();
      }

      return requestFileBlobs;
    }),
    mergeMap((blobArray) => forkJoin(blobArray.map((blob) => from(blob))))
  );
}
