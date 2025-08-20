import {LawField} from './law-field.model';
import {PaymentAgreement} from './payment-agreement.model';

export interface LegalFileRequest {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  civicNumber: string;
  streetName: string;
  city: string;
  province: string;
  postalCode: string;
  birthDate: Date | null;
  startOfMandate: Date | null;
  lawField: LawField | null;
  agreementType: PaymentAgreement | null;
  fileCreator: string;
}
