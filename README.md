import { forkJoin, Observable } from 'rxjs';
import { mergeMap, toArray } from 'rxjs/operators';

getocRobservable(ocrRequest: OcrRequest): Observable<{ ocrizedFile: File, classifications: DocumentClassification[] }> {
  return this.documentocrRepository.ocrizeDocumentTwo(ocrRequest).pipe(
    mergeMap(splittedFilesArray => {
      const observables: Observable<{ ocrizedFile: File, classifications: DocumentClassification[] }>[] = [];

      splittedFilesArray.forEach(splittedFile => {
        const ocrizeDocumentObservable = this.documentocrRepository.ocrizeDocument(splittedFile);

        const classificationsObservable = ocrizeDocumentObservable.pipe(
          mergeMap(ocrizedFile =>
            this.documentclassificationRepository.getclassifications(ocrizedFile).pipe(
              toArray(), // Collect classifications into an array
              mergeMap(classifications => {
                const processedFile = OcrPdfUtils.mergeFiles([ocrizedFile]);
                return of({ ocrizedFile: processedFile, classifications });
              })
            )
          )
        );

        observables.push(classificationsObservable);
      });

      return forkJoin(observables).pipe(
        toArray(), // Collect all results into an array
        mergeMap(resultArray => {
          const files: File[] = [];
          const documentclassifications: DocumentClassification[] = [];

          resultArray.forEach(result => {
            console.log('ocrizedFile =>', result.ocrizedFile);
            files.push(result.ocrizedFile);
            documentclassifications.push(...result.classifications);
            console.log('classifications =>', result.classifications);
          });

          const processedFile = OcrPdfUtils.mergeFiles(files);

          // Return the final result
          return of({ ocrizedFile: processedFile, classifications: documentclassifications });
        })
      );
    })
  );
}

