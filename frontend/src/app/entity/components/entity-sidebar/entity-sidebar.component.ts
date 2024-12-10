import { Component, computed, Input, model, ModelSignal, signal, Signal, WritableSignal } from '@angular/core';
import { ISectionLink, ISectionLinkDisplay } from '../../models/entity';
import { combineLatest, map, take, tap } from 'rxjs';
import { SectionLinkDisplayState } from '../entity-widget/entity-widget.component';
import { Store } from '@ngrx/store';
import { CommonModule } from '@angular/common';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { RouterModule } from '@angular/router';
import { DamSideBarToggleComponent } from '@usnistgov/ngx-dam-framework';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-entity-sidebar',
  standalone: true,
  imports: [
    CommonModule,
    FaIconComponent,
    RouterModule,
    DamSideBarToggleComponent,
    FormsModule,
  ],
  templateUrl: './entity-sidebar.component.html',
  styleUrl: './entity-sidebar.component.scss'
})
export class EntitySidebarComponent {
  @Input({ required: true })
  set links(links: ISectionLink[]) {
    combineLatest(
      links.map((link) => SectionLinkDisplayState.findById(this.store, link.id))
    ).pipe(
      take(1),
      map((sectionLinkList) => sectionLinkList.filter((v) => !!v)),
      tap((sectionLinkList) => { this.sections.set(sectionLinkList) })
    ).subscribe();
  }

  filterText: ModelSignal<string> = model('');
  sections: WritableSignal<ISectionLinkDisplay[]> = signal([]);
  filteredSections!: Signal<ISectionLinkDisplay[]>;

  constructor(private store: Store) {
    this.filteredSections = computed(() => {
      const text = this.filterText();
      const sections = this.sections();
      if (text) {
        return sections.filter((section) => section.label && section.label.toLowerCase().includes(text.toLowerCase()));
      } else {
        return sections;
      }
    });
  }

}
