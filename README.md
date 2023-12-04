import { Observable, forkJoin, concat, from, of } from 'rxjs';
import { mergeMap, toArray, map } from 'rxjs/operators';

private getocrizedDocumentForEachchunk(ocrRequest: OcrRequest): Observable<File[]> {
  return this.prepareocrRequests(ocrRequest).pipe(
    mergeMap(ocrRequests => {
      const batchSize = 2; // Set your desired batch size

      // Process requests in batches sequentially
      const batches: Observable<File[]>[] = [];
      let currentIndex = 0;

      while (currentIndex < ocrRequests.length) {
        const batch = ocrRequests.slice(currentIndex, currentIndex + batchSize);
        currentIndex += batchSize;

        const batchFiles$ = from(batch).pipe(
          mergeMap(request => this.getocrizeDocument(request)),
          toArray()
        );

        batches.push(batchFiles$);
      }

      // Combine results from all batches
      return forkJoin(batches).pipe(
        // Flatten the array of arrays into a single array
        map(results => [].concat(...results))
      );
    })
  );
}

