import { NgModule } from '@angular/core';

import { GestSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [GestSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [GestSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class GestSharedCommonModule {}
