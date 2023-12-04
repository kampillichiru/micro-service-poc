import { Observable, forkJoin } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

private getocrizedDocumentForEachchunk(ocrRequest: OcrRequest): Observable<File[]> {
  return this.prepareocrRequests(ocrRequest).pipe(
    mergeMap(ocrRequests => {
      const batchSize = 2; // Set your desired batch size

      const processBatch = (batch: OcrRequest[]) => {
        const batchFiles: Observable<File>[] = [];

        batch.forEach(request => {
          batchFiles.push(this.getocrizeDocument(request));
        });

        return forkJoin(batchFiles);
      };

      const batches: Observable<File[]>[] = [];
      let currentIndex = 0;

      while (currentIndex < ocrRequests.length) {
        const batch = ocrRequests.slice(currentIndex, currentIndex + batchSize);
        currentIndex += batchSize;

        batches.push(processBatch(batch));
      }

      // Combine results from all batches
      return forkJoin(batches).pipe(
        // Flatten the array of arrays into a single array
        mergeMap(results => results)
      );
    })
  );
}
