import { Routes } from '@angular/router';
import {appGuardGuard} from './app-guard.guard';

export const routes: Routes = [
  {
    path: '',
    title: 'S.U.I.T.S | Accueil',
    loadComponent: () => import('./home/component/homepage.component').then(m => m.HomepageComponent),
  },
  {
    path: 'login',
    title: 'S.U.I.T.S | Connexion',
    loadComponent : () => import('./login/login-page/login-page.component').then(m => m.LoginPageComponent)},
  {
    path: 'register',
    title: 'S.U.I.T.S | Nouveau compte',
    children: [
      {
        path: 'professional',
        loadComponent: () => import("./register/page/register-page-professional/register-page-professional.component").then(m => m.RegisterPageProfessionalComponent)
      },
      {
        path: 'client',
        loadComponent: () => import('./register/page/register-page-client/register-page-client.component').then(m => m.RegisterPageClientComponent)},
    ]
  },
  {
    path: 'app',
    title: 'S.U.I.T.S | App',
    children: [
      {
        path: 'organisation',
        title: 'S.U.I.T.S | Organisation',
        loadComponent: () => import('./organisation/organisation-page/organisation-page.component').then(m => m.OrganisationPageComponent),
        canActivate: [appGuardGuard]
      },
      {
        path: 'cases/:documentId',
        title: 'S.U.I.T.S | Dossier',
        loadComponent: () => import("./case/case-detail/case-detail.component").then(m => m.CaseDetailComponent),
        canActivate: [appGuardGuard]
      },
      {
        path: 'tasks',
        title: 'S.U.I.T.S | Taches',
        canActivate: [appGuardGuard],
        loadComponent: () => import("./dashboard-pro/dashboard-pro-page/dashboard-pro-page.component").then(m => m.DashboardProPageComponent),
      },
      {
        path: 'tasks/:taskId',
        title: 'S.U.I.T.S | Tâche',
        loadComponent : () => import('./task/components/task-detail/task-detail.component').then(m => m.TaskDetailComponent),
        canActivate: [appGuardGuard]
      },
      {
        path: 'profile',
        title: 'S.U.I.T.S | Profil',
        loadComponent: () => import('./profile/profile.component').then(m => m.ProfileComponent),
        canActivate: [appGuardGuard]
      },
      {path: 'disconnect', redirectTo:'/login', pathMatch: 'full'},
    ]
  },
  {
    path: '**',
    title: '404 | Introuvable',
    loadComponent: () => import('./not-found-page/not-found-page.component').then(m => m.NotFoundPageComponent)
  },
];
