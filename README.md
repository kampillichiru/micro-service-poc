import { forkJoin, Observable, from } from 'rxjs';
import { mergeMap, toArray, map } from 'rxjs/operators';

getocRobservable(ocrRequest: OcrRequest): Observable<{ ocrizedFile: File, classifications: DocumentClassification[] }> {
  return this.documentocrRepository.ocrizeDocumentTwo(ocrRequest).pipe(
    mergeMap(splittedFilesArray => {
      const observables: Observable<{ ocrizedFile: Observable<Blob>, classifications: DocumentClassification[][] }>[] = [];

      splittedFilesArray.forEach(splittedFile => {
        const ocrizeDocumentObservable = this.documentocrRepository.ocrizeDocument(splittedFile);

        const classificationsObservable = ocrizeDocumentObservable.pipe(
          mergeMap(ocrizedFile =>
            this.documentclassificationRepository.getclassifications(ocrizedFile).pipe(
              toArray(), // Collect classifications into an array
              map(classifications => ({
                ocrizedFile: ocrizedFile, // Assuming ocrizedFile is Observable<Blob>
                classifications
              }))
            )
          )
        );

        observables.push(classificationsObservable);
      });

      return forkJoin(observables).pipe(
        toArray(), // Collect all results into an array
        mergeMap(resultArray => {
          const filesObservables: Observable<Blob>[] = [];
          const documentclassifications: DocumentClassification[] = [];

          resultArray.forEach(result => {
            console.log('ocrizedFile =>', result.ocrizedFile);
            filesObservables.push(result.ocrizedFile); // Push the Observable<Blob>
            documentclassifications.push(...result.classifications);
            console.log('classifications =>', result.classifications);
          });

          // Merge the observables into a single Blob
          return forkJoin(filesObservables).pipe(
            mergeMap(blobArray => {
              // Convert Blob array to a single Blob
              const mergedBlob = new Blob(blobArray, { type: 'application/pdf' });

              // Create a File from the merged Blob
              const processedFile = new File([mergedBlob], 'merged.pdf', { type: 'application/pdf' });

              // Return the final result
              return of({ ocrizedFile: processedFile, classifications: documentclassifications });
            })
          );
        })
      );
    })
  );
}
