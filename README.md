import { Observable, forkJoin, of, concat } from 'rxjs';
import { mergeMap, toArray } from 'rxjs/operators';

private getocrizedDocumentForEachchunk(ocrRequest: OcrRequest): Observable<File[]> {
  return this.prepareocrRequests(ocrRequest).pipe(
    mergeMap(ocrRequests => {
      const batchSize = 2; // Set your desired batch size

      const processBatch = (batch: OcrRequest[]) =>
        forkJoin(batch.map(request => this.getocrizeDocument(request)));

      const batches: Observable<File[]>[] = [];
      let currentIndex = 0;

      while (currentIndex < ocrRequests.length) {
        const batch = ocrRequests.slice(currentIndex, currentIndex + batchSize);
        currentIndex += batchSize;

        batches.push(of(batch).pipe(mergeMap(processBatch)));
      }

      // Concatenate the batches to ensure sequential processing
      return concat(...batches).pipe(
        // Flatten the array of arrays into a single array
        mergeMap(results => results)
      );
    })
  );
}

